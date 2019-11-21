package com.android.publiccourse;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class OBDataUtils {
    private volatile static OBDataUtils mDataUtils;
    private OBDataManager mOBDataManager;

    public static OBDataUtils getInstance(Context context) {
        if (mDataUtils == null) {
            mDataUtils = new OBDataUtils(context);
        }
        return mDataUtils;

    }

    private OBDataUtils(Context context) {
        mOBDataManager = new OBDataManager(context);
    }

    public String getQueryString(String key, String value) {
        return new StringBuffer(key).append("='").append(value).append("'").toString();
    }

    public String getQueryInt(String key, int value) {
        return new StringBuffer(key).append("=").append(value).toString();
    }

    public String getQueryLong(String key, int value) {
        return new StringBuffer(key).append("=").append(value).toString();
    }

    public String getArrayString(String... strings) {
        StringBuffer arrayBuffer = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            arrayBuffer.append(strings[i]);
            if (i == strings.length - 1) {
                return arrayBuffer.toString();
            } else {
                arrayBuffer.append(" and ");
            }
        }
        return arrayBuffer.toString();

    }

    // 用户帐户信息的保�?  
    public String mStrAccoutName = "";
    public String mStrAccoutCode = "";
    public String mIsSaveInfo = "";

    // �?��用户信息表里面是否有数据
    public boolean isCheckSaveAccout()

    {
        boolean temp = false;
        Cursor cursor = mOBDataManager.qurey(OBDataManager.LoginInfo.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mStrAccoutName = cursor.getString(cursor.getColumnIndex(OBDataManager.LoginInfo.USER_NAME));
                mStrAccoutCode = cursor.getString(cursor.getColumnIndex(OBDataManager.LoginInfo.USER_PWD));
                mIsSaveInfo = cursor.getString(cursor.getColumnIndex(OBDataManager.LoginInfo.USER_ISSAVED));
                temp = true;
            }
            cursor.close();
        }
        return temp;
    }

 

  




  
  
   
    /**
     * 增加符合条件的大图到数据�?     * 
     * @param sessionID
     * @param bigUrl
     * @param smallUrl
     * @return
     */

    public int insertBigBySessionidNum(String sessionID, String smallUrl, String bigUrl) {
        String whereArgs = OBDataManager.ChatMessageRecord.SESSION_ID + "='" + sessionID + "'" + " and " + OBDataManager.ChatMessageRecord.IMAGEPATH + "='" + smallUrl + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.ChatMessageRecord.IMAGEURL, bigUrl);
        return mOBDataManager.update(OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues, whereArgs, null);
    }

    /**
     * 增加符合条件的大图到数据�?     * 
     * @param sessionID
     * @param bigUrl
     * @param smallUrl
     * @return
     */

    public int insertMsgHistoryBySessionidNum(String ID, String smallUrl) {
        String whereArgs = OBDataManager.ChatMessageRecord._ID + "='" + ID + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.ChatMessageRecord.IMAGEPATH, smallUrl);
        return mOBDataManager.update(OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues, whereArgs, null);
    }

    /**
     * 更新具体每条信息的发送状�?     * 
     * @param sessionID
     * @param receiveTime
     * @param sendStatus
     * @return
     */
    public int updateSpecityItem(String ID, int sendStatus) {
        String whereArgs = OBDataManager.ChatMessageRecord._ID + "='" + ID + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.ChatMessageRecord.SEND_STATUS, sendStatus);
        return mOBDataManager.update(OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues, whereArgs, null);
    }

    /**
     * 通过好友验证的话进行好友验证信息的归属人的改�?     * 
     * @param sessionID
     * @param receiveTime
     * @param sendStatus
     * @return
     */
    public int updateSpecityItemCode(String sessionID, String messageCode, String messageFrom, String newMessageCode, String newMessageFrom, String newMessage) {
        String whereArgs = OBDataManager.ChatMessageRecord.SESSION_ID + "='" + sessionID + "'" + " and " + OBDataManager.ChatMessageRecord.CODE + "='" + messageCode + "'" + " and " + OBDataManager.ChatMessageRecord.WHERECOME + "='" + messageFrom + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.ChatMessageRecord.CODE, newMessageCode);
        contentValues.put(OBDataManager.ChatMessageRecord.WHERECOME, newMessageFrom);
        contentValues.put(OBDataManager.ChatMessageRecord.CONTENT, newMessage);
        return mOBDataManager.update(OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues, whereArgs, null);
    }

 
  

  
    /**
     * 根据sessionID查询未读消息数量
     * 
     * @param secondStatus
     * @param status
     * @return list<OBLFriendMessage>
     */

    public synchronized int queryUnReadMsgBySessionId(String sessionID) {

        Cursor cousor = mOBDataManager.queryUnReadMsgBySessionId(sessionID);
        int unReadNum = cousor.getCount();
        cousor.close();

        return unReadNum;
    }

    /**
     * 查询未读系统通知消息数量
     * 
     * @param secondStatus
     * @param status
     * @return list<OBLFriendMessage>
     */

    public synchronized int queryUnReadSysNCount(String status, String sessionid) {
        if (sessionid != null && status != null && !sessionid.equals("") && !status.equals("")) {
            Cursor cursor = mOBDataManager.qureyUnReadSysNCount(status, sessionid);
            int count = cursor.getCount();
            cursor.close();
            return count;
        }

        else
            return 0;
    }

    /**
     * 查询未读消息数量
     * 
     * @param secondStatus
     * @param status
     * @return list<OBLFriendMessage>
     */

    public synchronized int queryUnReadMsgCount(String status, String toUser) {
        if (toUser != null && status != null && !toUser.equals("") && !status.equals("")) {
            Cursor cursor = mOBDataManager.qureyUnReadMessageCount(status, toUser);
            int count = cursor.getCount();
            cursor.close();
            return count;
        }

        else
            return 0;
    }

    /**
     * 根据sessionID删除消息记录
     * 
     * @param sessionID
     * @return int
     */
    public synchronized int deleteMessageBySessionid(String sessionID) {
        String where = OBDataManager.ChatMessageRecord.SESSION_ID + "='" + sessionID + "'";
        return mOBDataManager.delete(OBDataManager.ChatMessageRecord.TABLE_NAME, where, null);
    }

    /**
     * 根据sessionID删除消息记录
     * 
     * @param sessionID
     * @return int
     */
    public synchronized int deleteMessageBySessionid(String[] args) {
        // String where = OBDataManager.ChatMessageRecord.SESSION_ID + "='"+
        // sessionID+"'";
        return mOBDataManager.deleteMsgBatch(args);
    }

    public synchronized void updateMessageByMesgID(String mesgId) {
        String where = OBDataManager.NoticeMessageRecord.NID + "='" + mesgId + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.NoticeMessageRecord.READ_STATUS, "2");

        // return mOBDataManager.update(
        // OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues,
        // where, null);
        mOBDataManager.updateReadSysNoticeStatus(mesgId);
    }

    /**
     * 根据sessionID更新消息记录为已�?     * 
     * @param sessionID
     * @return int
     */
    public synchronized void updateMessageBySessionid(String sessionID) {
        String where = OBDataManager.ChatMessageRecord.SESSION_ID + "='" + sessionID + "'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBDataManager.ChatMessageRecord.READ_STATUS, "2");

        // return mOBDataManager.update(
        // OBDataManager.ChatMessageRecord.TABLE_NAME, contentValues,
        // where, null);
        mOBDataManager.updateReadStatus(sessionID);
    }

    private Cursor queryMsgSort(String toUser, String likeName) {

        return mOBDataManager.qureyRecentMsgLikeSort(toUser, likeName);
    }

    private Cursor queryMsgSort(String toUser) {

        return mOBDataManager.qureyRecentMsgSort(toUser);
    }

    private Cursor queryMessage() {

        return mOBDataManager.qurey(OBDataManager.ChatMessageRecord.TABLE_NAME, null, null, null, null, null, null);
    }



}
