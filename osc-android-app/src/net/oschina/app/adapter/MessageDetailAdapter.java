package net.oschina.app.adapter;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.MyLinkMovementMethod;
import net.oschina.app.widget.MyURLSpan;
import net.oschina.app.widget.TweetTextView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageDetailAdapter extends ListBaseAdapter {

	@Override
	protected boolean loadMoreHasBg() {
		return false;
	}

	@Override
	protected View getRealView(int position, View convertView,
			final ViewGroup parent) {
		final Comment item = (Comment) _data.get(position);
		int itemType = 0;
		if (item.getAuthorId() == AppContext.getInstance().getLoginUid()) {
			itemType = 1;
		}
		boolean needCreateView = false;
		ViewHolder vh = null;
		if (convertView == null) {
			needCreateView = true;
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (vh != null && (vh.type != itemType)) {
			needCreateView = true;
		}
		if (vh == null) {
			needCreateView = true;
		}

		if (needCreateView) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					itemType == 0 ? R.layout.list_cell_chat_from
							: R.layout.list_cell_chat_to, null);
			vh = new ViewHolder(convertView);
			vh.type = itemType;
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		// vh.name.setText(item.getAuthor());

		vh.content.setMovementMethod(MyLinkMovementMethod.a());
		vh.content.setFocusable(false);
		vh.content.setDispatchToParent(true);
		vh.content.setLongClickable(false);
		Spanned span = Html.fromHtml(item.getContent());
		vh.content.setText(span);
		MyURLSpan.parseLinkText(vh.content, span);

		vh.avatar.setAvatarUrl(item.getPortrait());
		vh.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());

		return convertView;
	}

	static class ViewHolder {
		int type;
		//@InjectView(R.id.iv_avatar)
		AvatarView avatar;
		//@InjectView(R.id.tv_name)
		TextView name;
		//@InjectView(R.id.tv_time)
		TextView time;
		//@InjectView(R.id.tv_content)
		TweetTextView content;

		ViewHolder(View view) {
			avatar = (AvatarView) view.findViewById(R.id.iv_avatar);
			name = (TextView) view.findViewById(R.id.tv_name);
			time = (TextView) view.findViewById(R.id.tv_time);
			content = (TweetTextView) view.findViewById(R.id.tv_content);
		}
	}
}
