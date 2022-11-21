package de.htwBerlin.ai.mediAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.htwBerlin.ai.mediAlarm.databinding.FragmentFirstBinding
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonAlarm.setOnClickListener {
            setAlarm()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            100,
            pendingIntent
        )

        Log.d("Medicine Reminder", "setAlarm")
    }
}