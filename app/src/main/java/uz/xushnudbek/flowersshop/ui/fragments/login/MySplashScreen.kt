package uz.xushnudbek.flowersshop.ui.fragments.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.databinding.FragmentSplashScreenBinding
import uz.xushnudbek.flowersshop.ui.activity.LaunchActivity
import uz.xushnudbek.flowersshop.ui.activity.MainActivity

@SuppressLint("CustomSplashScreen")
class MySplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as LaunchActivity).viewModel
        val isUserSignedIn = viewModel.isUserSignedIn()
        if (isUserSignedIn) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Handler().postDelayed({
                startActivity(intent)
            }, 1500)
        } else
            Handler().postDelayed({
                findNavController().navigate(R.id.action_mySplashScreen_to_firstScreenFragment)
            }, 1500)

    }

}