package net.oschina.app.bean;

import net.oschina.app.R;
import net.oschina.app.fragment.ActiveFragment;
import net.oschina.app.fragment.CommentFrament;
import net.oschina.app.fragment.MyInformationFragment;
import net.oschina.app.fragment.SoftWareTweetsFrament;
import net.oschina.app.fragment.TweetPubFragment;
import net.oschina.app.fragment.UserBlogFragment;
import net.oschina.app.fragment.UserCenterFragment;
import net.oschina.app.viewpagefragment.OpensourceSoftwareFragment;
import net.oschina.app.viewpagefragment.QuestViewPagerFragment;

public enum SimpleBackPage {

	COMMENT(1, R.string.actionbar_title_comment, CommentFrament.class),
	
	QUEST(2, R.string.actionbar_title_questions, QuestViewPagerFragment.class),
	
	TWEET_PUB(3, R.string.actionbar_title_tweetpub, TweetPubFragment.class),
	
	SOFTWARE_TWEETS(4, R.string.actionbar_title_softtweet, SoftWareTweetsFrament.class),
	
	USER_CENTER(5, R.string.actionbar_title_user_center, UserCenterFragment.class),
	
	USER_BLOG(6, R.string.actionbar_title_user_blog, UserBlogFragment.class),
	
	MY_INFORMATION(7, R.string.actionbar_title_my_information, MyInformationFragment.class),
	
	MY_ACTIVE(8, R.string.actionbar_title_active, ActiveFragment.class),
	
	OPENSOURCE_SOFTWARE(9, R.string.actionbar_title_softwarelist, OpensourceSoftwareFragment.class);
		
	private int title;
	private Class<?> clz;
	private int value;

	private SimpleBackPage(int value, int title, Class<?> clz) {
		this.value = value;
		this.title = title;
		this.clz = clz;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static SimpleBackPage getPageByValue(int val) {
		for (SimpleBackPage p : values()) {
			if (p.getValue() == val)
				return p;
		}
		return null;
	}
}
