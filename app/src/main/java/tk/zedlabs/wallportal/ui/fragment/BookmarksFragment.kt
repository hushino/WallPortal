package tk.zedlabs.wallportal.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import tk.zedlabs.wallportal.R
import tk.zedlabs.wallportal.repository.BookmarkImage
import tk.zedlabs.wallportal.ui.activity.DetailActivity
import tk.zedlabs.wallportal.util.BaseFragment
import tk.zedlabs.wallportal.util.BookmarkAdapter
import tk.zedlabs.wallportal.util.isConnectedToNetwork
import tk.zedlabs.wallportal.viewmodel.BookmarkViewModel

class BookmarksFragment : BaseFragment(), BookmarkAdapter.OnImageListener {

    private lateinit var viewAdapter: BookmarkAdapter
    private lateinit var viewManager: GridLayoutManager
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var list: List<BookmarkImage>

    override fun onImageClick(position: Int) {
        val intent = Intent(activity, DetailActivity::class.java)
        val imageDetails = list[position]
        val urlFull = imageDetails.imageUrlFull
        val urlRegular = imageDetails.imageUrlRegular
        val id = imageDetails.imageName
        intent.putExtra("url_large", urlFull)
        intent.putExtra("url_regular", urlRegular)
        intent.putExtra("id", id)
        intent.putExtra("Activity", "BookmarkActivity")
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (context?.isConnectedToNetwork()) {
            true -> if (textViewConnectivityBookmark.visibility == VISIBLE) textViewConnectivityBookmark.visibility = GONE
            false -> textViewConnectivityBookmark.visibility = VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        bookmarkViewModel = getViewModel { parametersOf() }

        viewManager = GridLayoutManager(this.context, 2)

        launch {
            context?.let {
                list = bookmarkViewModel.getBookMarkImages().asReversed()
                viewAdapter = BookmarkAdapter(list, this@BookmarksFragment)
            }
            recyclerViewBookmarked.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
}
