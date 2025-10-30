package com.anggapamb.assessmenttesteratani.base.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.anggapamb.assessmenttesteratani.utils.DialogLoader
import com.nuvyz.core.base.activity.CoreActivity

abstract class BaseActivity<VB: ViewBinding, VM: ViewModel>: CoreActivity<VB>() {
    protected abstract val viewModel: VM

    lateinit var dialogLoader: DialogLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogLoader = DialogLoader(supportFragmentManager)
    }

}