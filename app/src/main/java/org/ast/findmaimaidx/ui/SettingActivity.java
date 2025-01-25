package org.ast.findmaimaidx.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import org.ast.findmaimaidx.R;
import org.ast.findmaimaidx.service.GitHubApiService;
import org.ast.findmaimaidx.been.Release;
import org.ast.findmaimaidx.utill.GitHubApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences settingProperties;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private TextInputEditText shuiyuEditText;
    private TextInputEditText luoxueEditText;
    private TextInputEditText userId;
    @SuppressLint({"SetTextI18n","MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingProperties = getSharedPreferences("setting", Context.MODE_PRIVATE);

        loadSettings();
        SwitchMaterial switchMaterial = findViewById(R.id.switchBeta1);
        switchMaterial.setChecked(settingProperties.getBoolean("setting_autobeta1", false));
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = settingProperties.edit();
            if (isChecked) {
                Toast.makeText(this, "已开启实验性功能,可能并不起作用", Toast.LENGTH_SHORT).show();
                editor.putBoolean("setting_autobeta1",true);
            }else {
                editor.putBoolean("setting_autobeta1",false);
            }
            editor.apply();
        });

        shuiyuEditText = findViewById(R.id.shuiyu);
        luoxueEditText = findViewById(R.id.luoxue);
        userId = findViewById(R.id.userId);

        MaterialButton saveButton = findViewById(R.id.save_settings_button);
        saveButton.setOnClickListener(v -> {
            saveSettings(switchMaterial.isChecked(), shuiyuEditText.getText().toString(), luoxueEditText.getText().toString(),userId.getText().toString());
        });
        MaterialButton changeButton = findViewById(R.id.changePhoto);
        changeButton.setOnClickListener(v -> openFileChooser());


        MaterialButton frifind = findViewById(R.id.getUserid);
        frifind.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, HackGetUserId.class);
            startActivity(intent);
        });
        TextView uuid = findViewById(R.id.uuid);
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        uuid.setText("Android ID:" + androidId);
        uuid.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建一个ClipData对象
            ClipData clip = ClipData.newPlainText("label", androidId);
// 将ClipData对象设置到剪贴板中
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Android ID已复制到剪贴板", Toast.LENGTH_SHORT).show();
        });
        TextView vits = findViewById(R.id.vits);
        vits.setText("App version:" + getAppVersionName()+"\nLatest version:" );
        getLatestRelease();
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private String getAppVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            SharedPreferences.Editor editor = settingProperties.edit();
            //先将图片复制到app目录下,再保存app目录下的uri
            try {
                File file = new File(getExternalFilesDir(null), "image.jpg");
                InputStream inputStream = getContentResolver().openInputStream(uri);
                OutputStream outputStream = Files.newOutputStream(file.toPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            }catch (Exception e) {}
            uri = Uri.fromFile(new File(getExternalFilesDir(null), "image.jpg"));
            editor.putString("image_uri", uri.toString());
            editor.apply();
            Toast.makeText(this, "图片已保存", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveSettings(boolean betaEnabled, String shuiyuUsername, String luoxueUsername,String userId) {
        SharedPreferences.Editor editor = settingProperties.edit();
        MaterialRadioButton materialRadioButton = findViewById(R.id.radioButton1);
        editor.putBoolean("setting_autobeta1", betaEnabled);
        editor.putString("shuiyu_username", shuiyuUsername);
        editor.putString("luoxue_username", luoxueUsername);
        editor.putString("userId", userId);
        editor.putInt("use_", materialRadioButton.isChecked()?0:1);
        editor.apply();
        Toast.makeText(this, "设置已保存,部分设置需要重启软件生效", Toast.LENGTH_SHORT).show();
    }

    private void loadSettings() {
        SwitchMaterial switchMaterial = findViewById(R.id.switchBeta1);
        shuiyuEditText = findViewById(R.id.shuiyu);
        luoxueEditText = findViewById(R.id.luoxue);
        MaterialRadioButton materialRadioButton = findViewById(R.id.radioButton1);
        MaterialRadioButton materialRadioButton2 = findViewById(R.id.radioButton2);

        switchMaterial.setChecked(settingProperties.getBoolean("setting_autobeta1", false));
        shuiyuEditText.setText(settingProperties.getString("shuiyu_username", ""));
        luoxueEditText.setText(settingProperties.getString("luoxue_username", ""));
        int use_ = settingProperties.getInt("use_", 0);
        if(use_==0) {
            materialRadioButton.setChecked(true);
        }else {
            materialRadioButton2.setChecked(true);
        }
        SharedPreferences mContextSp = this.getSharedPreferences(
                "updater.data",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSetting = settingProperties.edit();
        String username = mContextSp.getString("username", "");
        if(Objects.requireNonNull(shuiyuEditText.getText()).toString().isEmpty()) {
            if (mContextSp.contains("username")) {
                editorSetting.putString("shuiyu_username",username);
                editorSetting.apply();
                shuiyuEditText.setText(username);
            }
        }
    }
    private void openAppSettings() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Please go to settings and enable the required permissions.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void getLatestRelease() {
        GitHubApiService service = GitHubApiClient.getClient();
        Call<Release> call = service.getLatestRelease("Spaso1", "FindMaimaiDX_Phone"); // 替换为你的仓库信息

        call.enqueue(new Callback<Release>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Release> call, @NonNull Response<Release> response) {
                if (response.isSuccessful()) {
                    Release release = response.body();
                    if (release != null) {
                        String tagName = release.getTagName();
                        String name = release.getName();
                        String body = release.getBody();
                        String htmlUrl = release.getHtmlUrl();
                        TextView textView = findViewById(R.id.vits);
                        textView.setText(textView.getText() + tagName + "\n" + name + "\n" + body);
                        //Toast.makeText(SettingActivity.this, "Latest Release:\nTag Name: " + tagName + "\nName: " + name + "\nBody: " + body + "\nHTML URL: " + htmlUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "No release found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, "Failed to get release: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Release> call, @NonNull Throwable t) {
                Toast.makeText(SettingActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
