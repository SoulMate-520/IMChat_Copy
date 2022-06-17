package com.example.imchat.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.imchat.R;
import com.example.imchat.adapter.ContactAdapter;
import com.example.imchat.base.BaseFragment;
import com.example.imchat.bean.ContactBean;
import com.example.imchat.contact.model.IContactModel;
import com.example.imchat.contact.presenter.ContactPresenter;
import com.example.imchat.contact.view.IContactsView;
import com.example.imchat.util.ActivityUtil;
import com.example.imchat.util.SortUtil;
import com.example.imchat.util.contactUtil.CustomItemDecoration;
import com.example.imchat.util.contactUtil.SideBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment2 extends BaseFragment implements IContactsView, IContactModel {

	@BindView(R.id.recyc_cont)
	RecyclerView mRecyclerView;
	@BindView(R.id.relat_new_friend)
	RelativeLayout mRelativeLayout;
	@BindView(R.id.side_bar)
	SideBar sideBar;
	private LinearLayoutManager layoutManager;
	private CustomItemDecoration decoration;
	private ContactAdapter adapter;
	private ContactPresenter presenter;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ContentFragment2() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ContentFragment2.
	 */
	// TODO: Rename and change types and number of parameters
	public static ContentFragment2 newInstance(String param1, String param2) {
		ContentFragment2 fragment = new ContentFragment2();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_content2;
	}

	@Override
	protected void initView() {
		mRecyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getContext()));
		mRecyclerView.addItemDecoration(decoration = new CustomItemDecoration(getContext()));
	}

	@Override
	protected void initData() {
		presenter = new ContactPresenter(this,this);

	}

	@Override
	public void setContactsList(List<ContactBean> userList) {
		adapter = new ContactAdapter(userList);
		mRecyclerView.setAdapter(adapter);
	}

	@Override
	public void initEvents(List<ContactBean> userList) {
		sideBar.setIndexChangeListener(new SideBar.indexChangeListener() {
			@Override
			public void indexChanged(String tag) {
				if (TextUtils.isEmpty(tag) || userList.size() <= 0) return;
				for (int i = 0; i < userList.size(); i++) {
					if (tag.equals(userList.get(i).getIndexTag())) {
						layoutManager.scrollToPositionWithOffset(i, 0);
						return;
					}
				}
			}
		});
	}

	@Override
	public void sortData(List<ContactBean> list) {
		SortUtil.sortData(list);
		String tagsStr = SortUtil.getTags(list);
		sideBar.setIndexStr(tagsStr);
		decoration.setDatas(list, tagsStr);
	}

	@Override
	public void onResume() {
		super.onResume();
		presenter.getContactsList();
	}
}