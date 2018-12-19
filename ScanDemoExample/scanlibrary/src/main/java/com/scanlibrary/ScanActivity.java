package com.scanlibrary;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.Locale;
import org.opencv.android.OpenCVLoader;

public class ScanActivity extends AppCompatActivity {

  public static final String EXTRA_BUTTON_CLOSE = "button_close";
  public static final String EXTRA_BRAND_IMG_RES = "title_img_res";
  public static final String EXTRA_TITLE = "title";
  public static final String EXTRA_LANGUAGE = "language";
  public static final String EXTRA_ACTION_BAR_COLOR = "ab_color";
  public static final String EXTRA_IMAGE_LOCATION = "img_location";
  public static final String RESULT_IMAGE_PATH = ScanFragment.RESULT_IMAGE_PATH;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
    OpenCVLoader.initDebug();
    int titleImgRes = getIntent().getExtras().getInt(EXTRA_BRAND_IMG_RES, 0);
    int abColor = getIntent().getExtras().getInt(EXTRA_ACTION_BAR_COLOR);
    boolean showButtonClose = getIntent().getBooleanExtra(EXTRA_BUTTON_CLOSE, false);
    String title = getIntent().getExtras().getString(EXTRA_TITLE);
    String locale = getIntent().getExtras().getString(EXTRA_LANGUAGE);

    if (locale != null) {
      Locale l = new Locale(locale);
      Locale.setDefault(l);
      Configuration config = new Configuration();
      config.locale = l;
      getBaseContext().getResources()
          .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (title != null && getSupportActionBar() != null) {
      toolbar.setTitle(title);
      getSupportActionBar().setDisplayShowTitleEnabled(true);
    } else if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    if (titleImgRes != 0 && getSupportActionBar() != null) {
      getSupportActionBar().setLogo(titleImgRes);
    }

    if (showButtonClose && getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          finish();
        }
      });
    }

    if (abColor != 0 && getSupportActionBar() != null) {
      getSupportActionBar().setBackgroundDrawable(
          new ColorDrawable(getResources().getColor(abColor)));
    }

    if (savedInstanceState == null) {
      Bundle args = new Bundle();
      if (getIntent().getExtras() != null) {
        args.putAll(getIntent().getExtras());
      }

      FragmentManager fragMan = getSupportFragmentManager();
      Fragment f = new ScanFragment();
      f.setArguments(args);
      FragmentTransaction fragTransaction = fragMan.beginTransaction();
      fragTransaction.replace(R.id.container, f, "scan_frag").commit();
    }
  }

  @Override public void onBackPressed() {
    ScanFragment scanFragment =
        (ScanFragment) getSupportFragmentManager().findFragmentByTag("scan_frag");
    if (scanFragment != null) {
      boolean exit = scanFragment.onBackPressed();
      if (exit) {
        super.onBackPressed();
      }
    } else {
      super.onBackPressed();
    }
  }
}