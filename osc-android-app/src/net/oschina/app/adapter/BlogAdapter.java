package net.oschina.app.adapter;

import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Blog;
import net.oschina.app.util.StringUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author HuangWenwei
 * 
 * @date 2014年9月29日
 */
public class BlogAdapter extends ListBaseAdapter {

	static class ViewHolder {
		
		@InjectView(R.id.tv_title) TextView title;
		@InjectView(R.id.tv_source)TextView source;
		@InjectView(R.id.tv_time)TextView time;
		@InjectView(R.id.tv_comment_count) TextView comment_count;
		@InjectView(R.id.iv_tip) ImageView tip;
		
		public ViewHolder(View view) {
			ButterKnife.inject(this,view);
		}
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.list_cell_news, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Blog blog = (Blog) _data.get(position);

		vh.title.setText(blog.getTitle());
		vh.source.setText(blog.getAuthor());
		vh.time.setText(StringUtils.friendly_time(blog.getPubDate()));
		vh.comment_count.setText(blog.getCommentCount() + "");
		return convertView;
	}
}
