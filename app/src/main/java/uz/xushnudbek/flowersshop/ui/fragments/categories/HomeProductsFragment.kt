package uz.xushnudbek.flowersshop.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.adapters.recyclerview.BestDealsRecyclerAdapter
import uz.xushnudbek.flowersshop.adapters.recyclerview.ProductsRecyclerAdapter
import uz.xushnudbek.flowersshop.databinding.FragmentHomeProductsBinding
import uz.xushnudbek.flowersshop.firebaseDatabase.FirebaseDb
import uz.xushnudbek.flowersshop.resource.Resource
import uz.xushnudbek.flowersshop.util.Constants.Companion.PRODUCT_FLAG
import uz.xushnudbek.flowersshop.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import uz.xushnudbek.flowersshop.model.Product
import uz.xushnudbek.flowersshop.ui.activity.MainActivity

class HomeProductsFragment : Fragment() {
    private lateinit var binding: FragmentHomeProductsBinding
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var bestDealsAdapter: BestDealsRecyclerAdapter
    private lateinit var productsAdapter: ProductsRecyclerAdapter

    private lateinit var list:ArrayList<Product>
    private val TAG = "HomeProductsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        viewModel = (activity as MainActivity).viewModel
        bestDealsAdapter = BestDealsRecyclerAdapter()
        productsAdapter = ProductsRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBestDeals.visibility = View.GONE

        setupBestDealsRecyclerView()
        observeBestDeals()

        setupAllProductsRecyclerView()
        observeAllProducts()

        headerPaging()
        bestDealsPaging()
        productsPaging()

        observeEmptyHeader()
        observeEmptyBestDeals()

        onBestDealsProductClick()

        observeAddHeaderProductsToCart()


        productsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putSerializable("product", product)
            bundle.putString("flag", PRODUCT_FLAG)
            findNavController().navigate(
                R.id.action_homeFragment_to_productPreviewFragment,
                bundle
            )
        }


    }

    private fun observeAddHeaderProductsToCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showTopScreenProgressbar()
                    return@Observer
                }

                is Resource.Success -> {
                    hideTopScreenProgressbar()
                    val snackBarPosition = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
                    Snackbar.make(snackBarPosition,requireContext().getText(R.string.product_added),2500).show()
                    return@Observer
                }

                is Resource.Error -> {
                    hideTopScreenProgressbar()
                    return@Observer
                }
                else -> {}
            }
        })
    }

    private fun hideTopScreenProgressbar() {

    }

    private fun showTopScreenProgressbar() {

    }

    private fun onBestDealsProductClick() {
        bestDealsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putSerializable("product", product)
            findNavController().navigate(
                R.id.action_homeFragment_to_productPreviewFragment,
                bundle
            )

        }
    }

    private fun observeEmptyBestDeals() {
        viewModel.emptyBestDeals.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    rvBestDeals.visibility = View.GONE
                    tvBestDeals.visibility = View.GONE
                }
            }
        })
    }

    private fun observeEmptyHeader() {
        viewModel.emptyClothes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    rvAds.visibility = View.GONE
                }
            }
        })
    }


    private fun bestDealsPaging() {
        binding.rvBestDeals.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    viewModel.getBestDealsProduct()
                }
            }
        })
    }

    private fun headerPaging() {
        binding.rvAds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dx != 0) {
                    viewModel.getClothesProducts()
                }
            }
        })
    }

    private fun productsPaging() {
        binding.scrollChair.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getHomeProduct(productsAdapter.differ.currentList.size)
            }
        })
    }

    private fun observeAllProducts() {
        productsAdapter.differ.submitList(saveData())
    }

    private fun hideBottomLoading() {
       // binding.progressbar2.visibility = View.GONE
        binding.tvBestProducts.visibility = View.VISIBLE

    }

    private fun showBottomLoading() {
       // binding.progressbar2.visibility = View.VISIBLE
        binding.tvBestProducts.visibility = View.GONE
    }

    private fun setupAllProductsRecyclerView() {
        binding.rvChairs.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = productsAdapter
        }
    }


    private fun setupBestDealsRecyclerView() {
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }




    private fun observeBestDeals() {
        viewModel.bestDeals.observe(viewLifecycleOwner, Observer { bestDealsList ->
            bestDealsAdapter.differ.submitList(bestDealsList.toList())
            binding.tvBestDeals.visibility = View.VISIBLE
        })
    }

    private fun saveData(): List<Product>{
        val image = "https://firebasestorage.googleapis.com/v0/b/flowers-shop-25f44.appspot.com/o/products%2Fimages%2F45762669-e1e2-4236-9d37-3922ce7aed21?alt=media&token=3d67199c-1ea5-435d-907d-d1684c1d28bd"
            list = ArrayList()
        for (i in 0..10){
            val product = Product(
                i,
                "Rose ${i+1}",
                "Fragrant",
                "Flower",
                "100",
                "150",
                "0.9",
                image,
                10,
                null,
                null
            )
            list.add(product)
        }
        return list
    }

}