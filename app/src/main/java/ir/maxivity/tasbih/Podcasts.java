package ir.maxivity.tasbih;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.appunite.appunitevideoplayer.PlayerActivity;

import ir.maxivity.tasbih.fragments.ImamRezaDialogFragment;
import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;

public class Podcasts extends BaseFragment implements ImamRezaDialogFragment.OnCardClick {


    CardView imamReza;
    CardView imamHosein, abolFazl;

    private final String sahneJame = "https://tv.razavi.ir/hls/jame_2.m3u8";
    private final String sahneJomhori = "https://tv.razavi.ir/hls/jomhori_2.m3u8";
    private final String sahneEnghelab = "https://tv.razavi.ir/hls/enghelab_2.m3u8";
    private final String sahneAzadi = "https://tv.razavi.ir/hls/azadi_2.m3u8";
    private final String Rozeh = "https://tv.razavi.ir/hls/rozeh_2.m3u8";
    private final String Gonbad = "https://tv.razavi.ir/hls/gonbad_2.m3u8";
    private final String tv2 = "https://tv.razavi.ir/hls/tv_2.m3u8";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_ziarat_online, container, false);
        imamReza = root.findViewById(R.id.imam_reza);


        imamReza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new ImamRezaDialogFragment();
                dialog.show(getChildFragmentManager(), "IMAM REZA");
            }
        });

        imamHosein = root.findViewById(R.id.imam_hosein);
        abolFazl = root.findViewById(R.id.abol_fazl);

        imamHosein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOnline(Gonbad, "حرم امام حسین (َع)");
            }
        });

        abolFazl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOnline(Rozeh, "حرم ابولفضل");
            }
        });

        return root;
    }


    private void startOnline(String url, String title) {
        startActivity(PlayerActivity.getVideoPlayerIntent(getContext(),
                url,
                title));
    }

    @Override
    public void onSahnClick(SahnTypes sahn) {
        switch (sahn) {
            case JAME:
                startOnline(sahneJame, getString(R.string.sahn_jame));
                break;
            case AZADI:
                startOnline(sahneAzadi, getString(R.string.sahn_azadi));
                break;
            case JOMHORI:
                startOnline(sahneJomhori, getString(R.string.sahn_jomhuri));
                break;
            case ENGHELAB:
                startOnline(sahneEnghelab, getString(R.string.sahn_enghelab));
                break;
        }
    }
}
