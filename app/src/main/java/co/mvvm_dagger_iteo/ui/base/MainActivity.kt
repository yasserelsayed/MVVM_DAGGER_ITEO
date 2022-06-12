package co.mvvm_dagger_iteo.ui.base

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.View.GONE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import co.mvvm_dagger_iteo.R
import kotlinx.android.synthetic.main.custom_loading.*
import kotlinx.android.synthetic.main.popup_container.*

class MainActivity : AppCompatActivity() {

    private lateinit var mFragmentManager: FragmentManager
    lateinit var slideDown: Animation
    lateinit var slideUp: Animation
    private var mCurrentPopupFrag: Fragment?=null
    private var mErrorDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111)

        slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
        slideUp = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        mFragmentManager = supportFragmentManager
    }

    override fun onResume() {
        super.onResume()
        img_close.setOnClickListener {
            closePopup()
        }
        slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
        slideDown.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                crd_popup_container.visibility = GONE
            }
            override fun onAnimationRepeat(animation: Animation?) {
                return
            }
        })

    }

     fun showErrorDialog(Message: String) {
        if (mErrorDialog == null) {
            val mAlertDialogBuilder = AlertDialog.Builder(this)
            mAlertDialogBuilder.setMessage(Message)
            mAlertDialogBuilder.setPositiveButton(getString(R.string.tag_close)) { d, _ -> d.dismiss() }
            mErrorDialog = mAlertDialogBuilder.create()
        } else mErrorDialog?.setMessage(Message)
        mErrorDialog?.show()
    }

    fun initialPopup(mfragment: Fragment) {
        closePopup()
        if (lnr_fade_screen != null) {
            lnr_fade_screen.visibility = View.VISIBLE
            crd_popup_container.startAnimation(slideUp)
        }
        this.mCurrentPopupFrag = mfragment
        val fragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frm_popup_container, mfragment)
        fragmentTransaction.commit()
    }

    fun closePopup() {
        if (this.mCurrentPopupFrag == null || lnr_fade_screen == null) return
        crd_popup_container.startAnimation(slideDown)
        val fragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.remove(this.mCurrentPopupFrag!!)
        fragmentTransaction.commit()
    }


    fun closeAllKeyBoards() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            if (currentFocus != null)
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun showLoading() {
        rlt_loader?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        rlt_loader?.visibility = View.GONE
    }
}