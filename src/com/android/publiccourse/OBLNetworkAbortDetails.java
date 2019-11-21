package com.android.publiccourse;

import com.android.svod.R;

import android.os.Bundle;


/**
 * 网络连接不可�?-查看详情
 * 
 * @ClassName: OBNetworkAbortDetails
 * @Description: TODO
 * @author gaoxin
 * @date 2013-3-28 上午10:52
 * 
 */
public class OBLNetworkAbortDetails extends OBLServiceMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setClassName(this);
        super.onCreate(savedInstanceState);
        setTitleBarContentView(R.layout.ob_networkabortdetails);
    }
}
