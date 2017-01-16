package com.samoryka.verecipesclient.Views.DailyRecipes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Web.VeRecipesService;
import com.samoryka.verecipesclient.Web.WebRequestManager;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyRecipesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private SwipePlaceHolderView mSwipeView;
    private Button mAceptBtn;
    private Button mRejectBtn;
    private View mPlaceHolderView;
    private View mView;
    private Context mContext;

    private VeRecipesService veRecipesService;

    public DailyRecipesFragment() {
    }   // Required empty public constructor

    /**
     * Returns an instance of a DailyRecipesFragment
     *
     * @param service
     * @return
     */
    public static DailyRecipesFragment newInstance(VeRecipesService service) {
        DailyRecipesFragment fragment = new DailyRecipesFragment();
        fragment.veRecipesService = service;
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View elements mapping
        mView = inflater.inflate(R.layout.fragment_daily_recipes, container, false);
        mContext = mView.getContext();
        mSwipeView = (SwipePlaceHolderView) mView.findViewById(R.id.swipeView);
        mAceptBtn = (Button) mView.findViewById(R.id.acceptBtn);
        mRejectBtn = (Button) mView.findViewById(R.id.rejectBtn);
        mPlaceHolderView = mView.findViewById(R.id.emptyStackPlaceHolder);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.recipe__daily_card_swipe_in_message_view)
                        .setSwipeOutMsgLayoutId(R.layout.recipe_daily_card_swipe_out_message_view));

        // Recipes loading
        WebRequestManager.dailyRecipesAsSwipePlaceHolderView(veRecipesService, Calendar.getInstance().getTime(), mSwipeView, mContext,
                this);


        // OnClickListeners
        mView.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        mView.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
        // Inflate the layout for this fragment
        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Checks the number of cards in the view, in order ro make the placeholder appear at the right time
     */
    public void checkCardsLeft() {
        // If the card that's swiped is the last one, we hide the non-placeholder views
        if (mSwipeView.getChildCount() > 1) {
            mSwipeView.setVisibility(View.VISIBLE);
            mAceptBtn.setVisibility(View.VISIBLE);
            mRejectBtn.setVisibility(View.VISIBLE);
            mPlaceHolderView.setVisibility(View.GONE);
        } else {
            mSwipeView.setVisibility(View.GONE);
            mAceptBtn.setVisibility(View.GONE);
            mRejectBtn.setVisibility(View.GONE);
            mPlaceHolderView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
