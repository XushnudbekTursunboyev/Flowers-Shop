package uz.xushnudbek.flowersshop.ui.fragments.shopping.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import uz.xushnudbek.flowersshop.BuildConfig
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.databinding.FragmentProfileBinding
import uz.xushnudbek.flowersshop.model.User
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.util.Constants.Companion.UPDATE_ADDRESS_FLAG
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import uz.xushnudbek.flowersshop.ui.activity.LaunchActivity
import uz.xushnudbek.flowersshop.ui.activity.MainActivity


class ProfileFragment : Fragment() {
    val TAG = "ProfileFragment"
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        viewModel.getUser()
    }

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onHomeClick()
        onLogoutClick()
        onBillingAndAddressesClick()
        onProfileClick()
        onAllOrderClick()
        onTrackOrderClick()
        onLanguageClick()
        onHelpClick()

        observeProfile()
        binding.tvVersionCode.text =
            "${resources.getText(R.string.g_version)} ${BuildConfig.VERSION_NAME}"

    }

    private fun onHelpClick() {
        binding.linearHelp.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_helpFragment)
        }
    }


    private fun onLanguageClick() {
        binding.linearLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_languageFragment)
        }
    }

    private fun onTrackOrderClick() {
        binding.linearTrackOrder.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.g_coming_soon),Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun onAllOrderClick() {
        binding.allOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }
    }

    private fun onProfileClick() {
        binding.constraintProfile.setOnClickListener {
            user?.let {
                val bundle = Bundle()
                bundle.putSerializable("user",user)
                findNavController().navigate(R.id.action_profileFragment_to_editUserInformationFragment,bundle)
            }
        }


    }

    var user: User?=null
    private fun observeProfile() {
        viewModel.profile.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    hideLoading()
                    val user = response.data
                    this.user = user
                    binding.apply {
                        tvUserName.text = user?.firstName + " " + user?.lastName
                        Glide.with(requireView()).load(user?.imagePath)
                            .error(R.drawable.ic_default_profile_picture).into(binding.imgUser)
                    }
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            binding.progressbarSettings.visibility = View.GONE
            constraintParnet.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            binding.progressbarSettings.visibility = View.VISIBLE
            constraintParnet.visibility = View.INVISIBLE
        }
    }

    private fun onBillingAndAddressesClick() {
//        binding.linearBilling.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("clickFlag", UPDATE_ADDRESS_FLAG)
//            findNavController().navigate(R.id.action_profileFragment_to_billingFragment, bundle)
//        }
    }

    private fun onLogoutClick() {

        binding.linearOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LaunchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun onHomeClick() {
        val btm = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
            activity?.onBackPressed()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation?.visibility = View.VISIBLE
    }


}