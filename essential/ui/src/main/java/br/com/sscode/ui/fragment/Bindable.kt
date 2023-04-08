package br.com.sscode.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface Bindable<T: ViewBinding> {

    val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T
    var binding: T

    fun bindView(binding: T)

    fun bind(binding: T) {
        this.binding = binding
        bindView(binding)
    }

    fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachedToParent: Boolean) {
        binding = bindingInflater(inflater, container, attachedToParent)
        bind(binding)
    }
}
