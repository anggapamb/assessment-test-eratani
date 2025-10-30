package com.anggapamb.assessmenttesteratani.utils

import androidx.fragment.app.FragmentManager
import com.anggapamb.assessmenttesteratani.ui.dialog.ProgressDialog
import com.nuvyz.core.data.model.response.ApiStatus

class DialogLoader(
    private val fragmentManager: FragmentManager
) {

    private var dialog: ProgressDialog? = null

    fun listen(status: Int) {
        when (status) {
            ApiStatus.LOADING -> show()
            ApiStatus.SUCCESS, ApiStatus.ERROR -> dismiss()
        }
    }

    fun show(isShow: Boolean) {
        if (isShow) {
            show()
        } else {
            dismiss()
        }
    }

    private fun show() {
        if (dialog?.isAdded == true || fragmentManager.findFragmentByTag(TAG) != null) return

        dialog = ProgressDialog()
        dialog?.isCancelable = false
        dialog?.show(fragmentManager, TAG)
    }

    private fun dismiss() {
        dialog?.dismissAllowingStateLoss()
        dialog = null
    }

    companion object {
        private const val TAG = "progressDialog"
    }
}
