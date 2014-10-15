package net.oschina.app.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.adapter.BlogAdapter;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Blog;
import net.oschina.app.bean.BlogList;
import net.oschina.app.bean.ListEntity;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import android.view.View;
import android.widget.AdapterView;

/**
 * @author HuangWenwei
 *
 * @date 2014年10月10日
 */
public class BlogFragment extends BaseListFragment {
	
	protected static final String TAG = BlogFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "bloglist_";

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new BlogAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		BlogList list = XmlUtils.toBean(BlogList.class, is);
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((BlogList) seri);
	}

	@Override
	protected void sendRequestData() {
		OSChinaApi.getBlogList(blogType, mCurrentPage, mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Blog blog = (Blog) mAdapter.getItem(position);
		if (blog != null)
			UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
	}

}
