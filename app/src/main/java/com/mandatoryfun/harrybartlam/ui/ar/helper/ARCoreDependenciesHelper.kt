package com.mandatoryfun.harrybartlam.ui.ar.helper

import android.app.Activity
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.mandatoryfun.harrybartlam.ui.ar.helper.ARCoreDependenciesHelper.Result.Failure

object ARCoreDependenciesHelper {
    private var shouldRequestARCoreInstall = true

    fun isARCoreIsInstalled(activity: Activity): Result {
        try {
            val requestInstall = ArCoreApk.getInstance().requestInstall(activity, shouldRequestARCoreInstall)
            // We only care about 'INSTALL_REQUESTED'. Nothing to do if the returned value is 'INSTALLED'
            if (requestInstall == ArCoreApk.InstallStatus.INSTALL_REQUESTED) {
                shouldRequestARCoreInstall = false
                return Failure("ARCore SDK install requested")
            }

        } catch (exception: Exception) {
            return Failure(messageFromExceptionType(exception))
        }
        return Result.Success
    }

    private fun messageFromExceptionType(exception: Exception): String {
        return when (exception) {
            is UnavailableUserDeclinedInstallationException -> "Please install ARCore"
            is UnavailableArcoreNotInstalledException -> "Please install ARCore"
            is UnavailableApkTooOldException -> "Please update ARCore"
            is UnavailableSdkTooOldException -> "Please update this app"
            is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
            else -> "Failed to create AR session"
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(val message: String) : Result()
    }

}
