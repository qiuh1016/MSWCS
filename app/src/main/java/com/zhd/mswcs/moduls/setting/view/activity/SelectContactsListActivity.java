package com.zhd.mswcs.moduls.setting.view.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.contact.ChineseToPinyinHelper;
import com.zhd.mswcs.common.weidgt.contact.LetterIndexView;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.friend.adapter.ContactsListViewAdapter;
import com.zhd.mswcs.moduls.friend.bean.ContactsInfoBean;
import com.zhd.mswcs.moduls.setting.bean.SendTelephoneListBean;
import com.zhd.mswcs.moduls.setting.presenter.EditEscalationCyclePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_select_import_contacts_listview)
public class SelectContactsListActivity extends BaseMvpActivity<MvpView,EditEscalationCyclePresenter> implements MvpView{
    private static final String TAG = SelectContactsListActivity.class.getSimpleName();
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.lv_contacts)
    private ListView lv_contacts;
    @ViewInject(R.id.show_letter_in_center)
    private TextView showLetter;
    @ViewInject(R.id.letter_index_view)
    private LetterIndexView letterIndexView;
    private List<ContactsInfoBean> contactsInfoList = new ArrayList<>();
    private ContactsListViewAdapter selectContactsListAdapter;

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;





    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("通讯录选择")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setRightText("保存")
                .setRightIcon(R.drawable.btn_green_bg)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ContactsInfoBean> list = selectContactsListAdapter.getDataList();
                        List<ContactsInfoBean> selectList = new ArrayList<>();
                        for(ContactsInfoBean contactsInfo:list){
                            if(contactsInfo.isSelected()){
                                selectList.add(contactsInfo);
                            }
                        }
                        if(selectList!=null&&selectList.size()>0){
//                            StringBuilder telStringBuilder = new StringBuilder();
//                            StringBuilder nameStringBuilder = new StringBuilder();
//                           for(ContactsInfoBean bean:selectList){
//                               if(StringUtils.equals(bean.getContactTelephone(),PreferencesUtils.getString(getApplicationContext(),"telephone"))){
//                                   continue;
//                               }else{
//                                   telStringBuilder.append(bean.getContactTelephone()+",");
//                                   nameStringBuilder.append(bean.getContactName()+",");
//                               }
//                           }
//                            String resultStr = telStringBuilder.toString();
//                            String telephone = resultStr.substring(0,resultStr.length()-1);
//                            PreferencesUtils.putString(SelectContactsListActivity.this,"sos_send_telephone",telephone);
//
//                            String nameStr = nameStringBuilder.toString();
//                            String name = nameStr.substring(0,nameStr.length()-1);
//                            PreferencesUtils.putString(SelectContactsListActivity.this,"sos_send_name",name);

                            for(ContactsInfoBean bean:selectList){
                               if(StringUtils.equals(bean.getContactTelephone(), PreferencesUtils.getString(getApplicationContext(),"telephone"))){
                                   continue;
                               }else{
                                   saveSOSTelephoneInfo(bean);
                               }
                           }

                            setResult(RESULT_OK);
                            finish();
                        }else{
                            ToastUtil.showToast(SelectContactsListActivity.this,"请先选择通讯录好友");
                        }

                    }
                })
                .build()
                .builder();
    }


    @Override
    public EditEscalationCyclePresenter createPresenter() {
        return new EditEscalationCyclePresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        letterIndexView.setTextViewDialog(showLetter);
        letterIndexView.setUpdateListView(new LetterIndexView.UpdateListView() {
            @Override
            public void updateListView(String currentChar) {
                int positionForSection = selectContactsListAdapter.getPositionForSection(currentChar.charAt(0));
                lv_contacts.setSelection(positionForSection);
            }
        });
        initAllContactsData();

    }

    private void initAllContactsData() {
        getPhoneContacts(this);
    }

    /**得到手机通讯录联系人信息**/
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void getPhoneContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                phoneNumber = phoneNumber.replace(" ","");
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.mipmap.img_user_head);
                }

                ContactsInfoBean contactsInfoBean = new ContactsInfoBean();
                contactsInfoBean.setContactName(contactName);
                contactsInfoBean.setContactPhoto(contactPhoto);
                contactsInfoBean.setContactTelephone(phoneNumber);
                if(!contactsInfoList.contains(contactsInfoBean)){
                    contactsInfoList.add(contactsInfoBean);
                }

            }
            phoneCursor.close();
            getSIMContacts(mContext);
        }
    }




    /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                phoneNumber = phoneNumber.replace(" ","");
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //Sim卡中没有联系人头像,所以显示默认头像
                Bitmap  contactPhoto = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.img_user_head);
                ContactsInfoBean contactsInfoBean = new ContactsInfoBean();
                contactsInfoBean.setContactName(contactName);
                contactsInfoBean.setContactPhoto(contactPhoto);
                contactsInfoBean.setContactTelephone(phoneNumber);
                if(!contactsInfoList.contains(contactsInfoBean)){
                    contactsInfoList.add(contactsInfoBean);
                }
            }
            phoneCursor.close();
            for (ContactsInfoBean bean : contactsInfoList) {
                String convert = ChineseToPinyinHelper.getInstance().getPinyin(bean.getContactName()).toUpperCase();
                if(convert.length()>0){
                    bean.setPinyin(convert);
                    String substring = convert.substring(0, 1);
                    if (substring.matches("[A-Z]")) {
                        bean.setFirstLetter(substring);
                    }else{
                        bean.setFirstLetter("#");
                    }
                }else{
                    bean.setFirstLetter("#");
                }

            }
            Collections.sort(contactsInfoList, new Comparator<ContactsInfoBean>() {
                @Override
                public int compare(ContactsInfoBean lhs, ContactsInfoBean rhs) {
                    if (lhs.getFirstLetter().contains("#")) {
                        return 1;
                    } else if (rhs.getFirstLetter().contains("#")) {
                        return -1;
                    }else{
                        return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
                    }
                }
            });

            selectContactsListAdapter = new ContactsListViewAdapter(this, contactsInfoList);
            lv_contacts.setAdapter(selectContactsListAdapter);
            lv_contacts.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int sectionForPosition = selectContactsListAdapter.getSectionForPosition(firstVisibleItem);
                    letterIndexView.updateLetterIndexView(sectionForPosition);
                }
            });

        }
    }


    private void saveSOSTelephoneInfo(ContactsInfoBean contactsInfoBean){
        SendTelephoneListBean bean = new SendTelephoneListBean();
        bean.setCurrentUser(PreferencesUtils.getString(this,"telephone"));
        bean.setIsDelete("0");
        bean.setCreateTime(TimeUtils.getCurTimeString());
        bean.setUpdateTime(TimeUtils.getCurTimeString());
        bean.setContactName(contactsInfoBean.getContactName());
        bean.setContactTelephone(contactsInfoBean.getContactTelephone());
        try {
            MyApplication.getInstance().getDb().saveOrUpdate(bean);
            setResult(RESULT_OK);
            finish();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
