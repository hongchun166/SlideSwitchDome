字母滑动 AlphabetView	

public class AlphabetDemoActivity extends AppCompatActivity {
    AlphabetView alphabetView;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_demo_activity);
         alphabetView= (AlphabetView) findViewById(R.id.alphabetView);
        textView= (TextView) findViewById(R.id.textView);
        alphabetView.setDefultImpOnTouchAssortListener();
        alphabetView.setOnTouchAssortListener(new OnTouchAssortListenerImp() {
            @Override
            public void onTouchAssortChanged(String s) {
                textView.setText(s);
            }
        });
    }
}

XML文件
    <thc.com.view.slideswitch.alphabetView.AlphabetView
        android:id="@+id/alphabetView"
        android:layout_width="50dp"
        android:layout_height="match_parent"
        android:layout_gravity="right"
        android:layout_alignParentRight="true"
        app:mAV_bgColor="#50000000"
        app:mAV_bgColorSelect="#90000000"
        app:mAV_textColor="#FFFFFF"
        app:mAV_textColorSelect="@android:color/holo_red_dark"
        app:mAV_textSize="16sp"
        app:mAV_textSizeSelect="18sp"
        app:mAV_popBgColor="@color/colorAccent"
        app:mAV_popH="100dp"
        app:mAV_popW="100dp"
        app:mAV_popTextColor="@android:color/holo_green_dark"
        app:mAV_popTextSize="20sp"
        />






# SlideSwitchDome
滑动开关

 * 滑动,小块开关
 * 站在巨人的肩膀上撸代码
 * http://blog.csdn.net/chziroy/article/details/44146911
 * 小滑块改为宽度一半
  添加文字，添加点击，添加小滑块小竖线




