@file:Suppress("unused", "RedundantVisibilityModifier", "UNCHECKED_CAST")

package com.alefimenko.iuttimetable.util

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import com.bluelinelabs.conductor.Controller
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import androidx.fragment.app.DialogFragment as SupportDialogFragment
import androidx.fragment.app.Fragment as SupportFragment

fun <V : View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> =
    required(id, viewFinder, null)

fun <V : View> Dialog.bindView(id: Int): ReadOnlyProperty<Dialog, V> =
    required(id, viewFinder, null)

fun <V : View> Activity.bindOptionalView(id: Int): ReadOnlyProperty<Activity, V?> =
    nullable(id, viewFinder, null)

fun <V : View> Dialog.bindOptionalView(id: Int): ReadOnlyProperty<Dialog, V?> =
    nullable(id, viewFinder, null)

fun <V : View> Activity.bindViews(ids: IntArray): ReadOnlyProperty<Activity, List<V>> =
    required(ids, viewFinder, null)

fun <V : View> Dialog.bindViews(ids: IntArray): ReadOnlyProperty<Dialog, List<V>> =
    required(ids, viewFinder, null)

fun <V : View> Activity.bindOptionalViews(ids: IntArray): ReadOnlyProperty<Activity, List<V>> =
    nullable(ids, viewFinder, null)

fun <V : View> Dialog.bindOptionalViews(ids: IntArray): ReadOnlyProperty<Dialog, List<V>> =
    nullable(ids, viewFinder, null)

fun <V : View> View.bindView(id: Int, viewInitializer: ViewInitializer<V>? = null) =
    requiredImmediate(id, viewFinder, viewInitializer)

fun <V : View> View.bindViews(ids: IntArray, viewInitializer: ViewInitializer<List<V>>? = null) =
    requiredImmediate(ids, viewFinder, viewInitializer)

fun <V : View> View.bindOptionalView(id: Int, viewInitializer: ViewInitializer<V>? = null) =
    nullableImmediate(id, viewFinder, viewInitializer)

fun <V : View> View.bindOptionalViews(
    ids: IntArray,
    viewInitializer: ViewInitializer<List<V>>? = null
) = nullableImmediate(ids, viewFinder, viewInitializer)

fun SupportFragment.createBinder() = ViewBinder(viewFinder)
fun SupportDialogFragment.createBinder() = ViewBinder(viewFinder)
fun Activity.createBinder() = ViewBinder(viewFinder)
fun Controller.createBinder() = ViewBinder(viewFinder)

class ViewBinder(private val viewFinder: Finder) {
    private val lazyRegistry = mutableListOf<Lazy<*>>()

    operator fun <V : View> invoke(
        @IdRes id: Int,
        viewFinder: Finder = this.viewFinder,
        viewInitializer: ViewInitializer<V>? = null
    ) = required(id, viewFinder, viewInitializer).register()

    operator fun <V : View> invoke(
        ids: IntArray,
        viewFinder: Finder = this.viewFinder,
        viewInitializer: ViewInitializer<List<V>>? = null
    ) = required(ids, viewFinder, viewInitializer).register()

    fun <V : View> optional(
        @IdRes id: Int,
        viewFinder: Finder = this.viewFinder,
        viewInitializer: ViewInitializer<V?>? = null
    ) = nullable(id, viewFinder, viewInitializer).register()

    fun <V : View> optional(
        ids: IntArray,
        viewFinder: Finder = this.viewFinder,
        viewInitializer: ViewInitializer<List<V?>>? = null
    ) = nullable(ids, viewFinder, viewInitializer).register()

    fun <T : Any> stuff(initializer: () -> T): ReadOnlyProperty<Any, T> =
        Lazy { initializer() }.register()

    fun resetViews() = lazyRegistry.forEach { it.reset() }

    private fun <V> Lazy<V>.register(): ReadOnlyProperty<Any, V> = also {
        lazyRegistry += it
    }
}

private typealias Finder = (Int) -> View?
private typealias ViewInitializer<V> = V.() -> Unit

private inline val View.viewFinder: Finder get() = { findViewById(it) }
private inline val Activity.viewFinder: Finder get() = { findViewById(it) }
private inline val Dialog.viewFinder: Finder get() = { findViewById(it) }
private inline val Controller.viewFinder: Finder get() = { view?.findViewById(it) }

private val SupportDialogFragment.viewFinder: Finder
    get() = { id ->
        if (dialog == null && view == null) {
            tooEarlyViewAccess(this)
        } else {
            dialog?.findViewById(id) ?: view?.findViewById(id)
        }
    }
private val SupportFragment.viewFinder: Finder
    get() = { (view ?: tooEarlyViewAccess(this)).findViewById(it) }

private fun <V : View> required(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) =
    Lazy { desc -> requiredImmediate(id, finder, viewInitializer, desc) }

private fun <V : View> nullable(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) =
    Lazy { nullableImmediate(id, finder, viewInitializer) }

private fun <V : View> required(
    ids: IntArray,
    finder: Finder,
    viewInitializer: ViewInitializer<List<V>>?
) = Lazy { desc -> requiredImmediate(ids, finder, viewInitializer, desc) }

private fun <V : View> nullable(
    ids: IntArray,
    finder: Finder,
    viewInitializer: ViewInitializer<List<V>>?
) = Lazy { nullableImmediate(ids, finder, viewInitializer) }

private fun <V : View> requiredImmediate(
    id: Int,
    finder: Finder,
    viewInitializer: ViewInitializer<V>?,
    name: String? = null
) = (finder(id) as V? ?: viewNotFound(id, name)).also { viewInitializer?.invoke(it) }

private fun <V : View> requiredImmediate(
    ids: IntArray,
    finder: Finder,
    viewInitializer: ViewInitializer<List<V>>?,
    name: String? = null
) = ids.map { finder(it) as V? ?: viewNotFound(it, name) }.also { viewInitializer?.invoke(it) }

private fun <V : View> nullableImmediate(
    id: Int,
    finder: Finder,
    viewInitializer: ViewInitializer<V>?
) = (finder(id) as V?)?.also { viewInitializer?.invoke(it) }

private fun <V : View> nullableImmediate(
    ids: IntArray,
    finder: Finder,
    viewInitializer: ViewInitializer<List<V>>?
) = ids.map { finder(it) as V? }.filterNotNull().also { viewInitializer?.invoke(it) }

private class Lazy<out V>(val initializer: (String?) -> V) : ReadOnlyProperty<Any, V> {
    private object EmptyValue

    private var value: Any? = EmptyValue

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        init(property.name)
        return value as V
    }

    fun init(name: String? = null) {
        if (value == EmptyValue) {
            value = initializer(name)
        }
    }

    fun reset() {
        value = EmptyValue
    }
}

private fun viewNotFound(id: Int, name: String? = null): Nothing =
    throw IllegalStateException("View with ID $id '${name?.let { "for $it" } ?: ""}' not found.")

private fun tooEarlyViewAccess(ref: Any): Nothing =
    throw IllegalStateException("You're accessing views of ${ref.javaClass.name} too early")
