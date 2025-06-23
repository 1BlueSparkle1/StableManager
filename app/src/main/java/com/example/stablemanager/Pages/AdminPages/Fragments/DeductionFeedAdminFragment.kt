package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.stablemanager.Components.Managers.FeedManager
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Notification
import java.time.LocalDate


class DeductionFeedAdminFragment : Fragment() {
    companion object{
        val TAG: String = DeductionFeedAdminFragment::class.java.simpleName
        fun newInstance() = DeductionFeedAdminFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_deduction_feed_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val feedManager = FeedManager(requireContext())
        val feedId = feedManager.getFeedId()
        val quantityEditText: EditText = view.findViewById(R.id.quantityDeductionFeedAdmin)
        val subtractButton: Button = view.findViewById(R.id.deductionQuantityFeedAdminButton)
        val titleFeed: TextView = view.findViewById(R.id.textViewNameFeedAdmin)
        var feed = db.getFeedById(feedId)
        titleFeed.text = "Вычетание ${feed!!.title}(кг)"

        subtractButton.setOnClickListener {
            val quantityString = quantityEditText.text.toString().trim()

            if (quantityString.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните количество!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val quantity = quantityString.toDouble()

                if (quantity <= 0) {
                    Toast.makeText(requireContext(), "Количество должно быть больше нуля!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                val success = db.subtractQuantityFromFeed(feedId, quantity)

                if (success) {
                    Toast.makeText(requireContext(), "Количество корма успешно вычтено!", Toast.LENGTH_SHORT).show()
                    quantityEditText.text.clear()


                    feed = db.getFeedById(feedId)
                    if(feed!!.quantity <= 5){
                        val stable = db.getStableById(feed!!.stableId)
                        if(stable != null){
                            Log.e("OptionsFragment", stable.ownerId.toString())
                            db.addNotifications(Notification( 0, "Корм - ${feed!!.title} в вашей конюшне ${stable.title} заканчивается, не забодьте пополнить запасы!",
                                false, null, stable.ownerId, LocalDate.now().toString(), null, null))
                        }
                    }

                    val activity = activity as? StartAdminPageActivity

                    if (activity != null) {
                        activity.replaceFragment(FeedListAdminFragment.newInstance(), FeedListAdminFragment.TAG)
                    } else {
                        Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                    }
                } else {
                    Toast.makeText(requireContext(), "Не удалось вычесть количество корма. Возможно, недостаточно корма.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Некорректный формат числа!", Toast.LENGTH_SHORT).show()
                Log.e("SubtractFeedFragment", "Ошибка преобразования числа: ${e.message}")
            }
        }

        return view
    }
}