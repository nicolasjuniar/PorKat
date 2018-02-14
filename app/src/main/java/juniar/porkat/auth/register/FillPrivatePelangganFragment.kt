package juniar.porkat.auth.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import juniar.porkat.R
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_pelanggan_private.*

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class FillPrivatePelangganFragment : BaseFragment<Any>() {

    lateinit var callback: RegisterView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as RegisterView
    }

    companion object {
        fun newInstance(): FillPrivatePelangganFragment {
            return FillPrivatePelangganFragment()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_register_pelanggan_private, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Observable.combineLatest(
                RxTextView.textChanges(et_fullname)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_phone)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_address)
                        .map { it.isNotEmpty() },
                Function3 { fullname: Boolean, phone: Boolean, address:Boolean ->
                    fullname && phone && address
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback.onFieldFilled(it)
                }
    }
}