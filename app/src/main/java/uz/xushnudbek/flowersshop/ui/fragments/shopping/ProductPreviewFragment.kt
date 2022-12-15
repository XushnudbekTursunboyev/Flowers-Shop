package uz.xushnudbek.flowersshop.ui.fragments.shopping

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.databinding.FragmentProductPreviewBinding
import uz.xushnudbek.flowersshop.model.CartProduct
import uz.xushnudbek.flowersshop.model.Product
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.ui.activity.MainActivity
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel

class ProductPreviewFragment : Fragment() {

    private var product: Product? = null
    val TAG = "ProductPreviewFragment"


    private lateinit var binding: FragmentProductPreviewBinding
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        arguments?.apply {
            product = this.getSerializable("product") as Product
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductPreviewBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.visibility = View.GONE

        //  setupViewpager()


        setProductInformation(product!!)
//        onImageCloseClick()
       onBtnAddToCartClick()
//
       observeAddToCart()
    }

    private fun observeAddToCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, Observer { response ->
            val btn = binding.btnAddToCart
            when (response) {
                is Resource.Loading -> {
                    startLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    stopLoading()
                    viewModel.addToCart.value = null
                    return@Observer
                }

                is Resource.Error -> {
                    Toast.makeText(activity, "Oops! error occurred", Toast.LENGTH_SHORT).show()
                    viewModel.addToCart.value = null
                    Log.e(TAG, response.message.toString())
                }
            }
        })
    }

    private fun stopLoading() {
        binding.apply {
            btnAddToCart.visibility = View.VISIBLE
            progressbar.visibility = View.INVISIBLE
        }
    }

    private fun startLoading() {
        binding.apply {
            btnAddToCart.visibility = View.INVISIBLE
            progressbar.visibility = View.VISIBLE
        }
    }


    private fun onBtnAddToCartClick() {
        binding.btnAddToCart.apply {
            setOnClickListener {

                val image = product?.images
                val cartProduct = CartProduct(
                    product!!.id,
                    product?.title!!,
                    product?.seller!!,
                    image!!,
                    product?.price!!,
                    product?.newPrice,
                    1
                )
                viewModel.addProductToCart(cartProduct)
                setBackgroundResource(R.color.g_black)
            }
        }
    }

    private fun onImageCloseClick() {
        binding.imgClose.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductInformation(product: Product) {
        val imagesList = product.images!!

        binding.apply {
            Glide.with(requireContext()).load(product.images).into(binding.viewpager2Images)

            tvProductName.text = product.title
            tvProductDescription.text = product.description
            tvProductPrice.text = "$${product.price}"
            tvProductOfferPrice.visibility = View.GONE
            product.newPrice?.let {
                if (product.newPrice.isNotEmpty() && product.newPrice != "0") {
                    tvProductPrice.paintFlags =
                        tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvProductOfferPrice.text = "$${product.newPrice}"
                    tvProductOfferPrice.visibility = View.VISIBLE
                }
            }
            product.sizeUnit?.let {
                if (it.isNotEmpty()) {
//                    binding.tvSizeUnit.visibility = View.VISIBLE
//                    binding.tvSizeUnit.text = " ($it)"
                }
            }
        }
    }


}