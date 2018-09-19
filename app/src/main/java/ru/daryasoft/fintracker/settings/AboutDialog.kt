package ru.daryasoft.fintracker.settings

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import ru.daryasoft.fintracker.R

class AboutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var version = "1.0"
        try {
            val pInfo = getActivity()!!.getPackageManager().getPackageInfo(getActivity()!!.getPackageName(), 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val aboutPage = AboutPage(getContext())
                .setDescription(getContext()!!.getResources().getString(R.string.about_description))
                .addItem(Element().setTitle("Version $version"))
                .addWebsite("http://divizdev.ru")
                .addGitHub("divizdev/fintracker")
                .create()


        return AlertDialog.Builder(getActivity()!!)
                .setTitle(R.string.title_dialog_about)
                .setView(aboutPage)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()

    }
}