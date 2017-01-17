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
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Utilities.StringFormatUtility;
import com.samoryka.verecipesclient.Web.RetrofitHelper;
import com.samoryka.verecipesclient.Web.VeRecipesService;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyRecipesFragment extends Fragment {

    @BindView(R.id.swipeView)
    SwipePlaceHolderView mSwipeView;
    @BindView(R.id.acceptBtn)
    Button mAceptBtn;
    @BindView(R.id.rejectBtn)
    Button mRejectBtn;
    @BindView(R.id.emptyStackPlaceHolder)
    View mPlaceHolderView;
    private OnFragmentInteractionListener mListener;
    private View mView;
    private Context mContext;

    private VeRecipesService veRecipesService = RetrofitHelper.initializeVeRecipesService();

    // self-reference to be able to add a recipe on this fragment in an inner class (ie in the HTTP request)
    private DailyRecipesFragment thisFragment = this;

    public DailyRecipesFragment() {
    }   // Required empty public constructor


    public static DailyRecipesFragment newInstance() {
        return new DailyRecipesFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //UI Elements setup
        mView = inflater.inflate(R.layout.fragment_daily_recipes, container, false);
        mContext = mView.getContext();
        ButterKnife.bind(this, mView);


        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.recipe__daily_card_swipe_in_message_view)
                        .setSwipeOutMsgLayoutId(R.layout.recipe_daily_card_swipe_out_message_view));

        loadRecipes();

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
     * Loads the current day's recipes into the stacked card view ("à la Tinder")
     */
    private void loadRecipes() {
        String dateString = StringFormatUtility.DateToYYYYMMDD(Calendar.getInstance().getTime());
        Observable<List<Recipe>> recipeListObservable = veRecipesService.listRecipesByDate(dateString);
        recipeListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Recipe>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Recipe> recipes) {

                        // We check the number of cards to add by seeing if the user has already swiped cards
                        // today
                        int firstIndex = 0;
                        long lastRecipeSwipedId = SharedPreferencesUtility.getLastSwipedRecipe(getContext());

                        String lastVisit = StringFormatUtility.DateToYYYYMMDD(SharedPreferencesUtility.getLastVisit(getContext()));
                        String today = StringFormatUtility.DateToYYYYMMDD(Calendar.getInstance().getTime());

                        if (lastVisit.equals(today)) {
                            for (int i = 0; i < recipes.size(); i++) {
                                if (recipes.get(i).getId() == lastRecipeSwipedId) {
                                    firstIndex = i + 1;
                                    break;
                                }
                            }
                        }

                        for (int i = firstIndex; i < recipes.size(); i++) {
                            mSwipeView.addView(new RecipeCard(mContext, recipes.get(i), mSwipeView, thisFragment));
                        }

                        checkCardsLeft();
                    }
                });

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
