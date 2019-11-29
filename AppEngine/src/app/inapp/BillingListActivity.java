package app.inapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.pnd.adshandler.R;
import app.server.v2.Billing;
import app.server.v2.BillingResponseHandler;
import app.server.v2.Slave;

/**
 * Created by quantum4u1 on 14/04/18.
 */

public class BillingListActivity extends Activity {

    private ArrayList<Billing> mBillingList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_list_layout);

        mBillingList = BillingResponseHandler.getInstance().getBillingResponse();

        FrameLayout mFreeLayout = findViewById(R.id.parentFree);
        FrameLayout mProLayout = findViewById(R.id.parentPro);
        FrameLayout mWeeklyLayout = findViewById(R.id.parentweekly);
        FrameLayout mMonthlyLayout = findViewById(R.id.parentMonthly);
        FrameLayout mHalfYearLayout = findViewById(R.id.parentHalfYear);
        FrameLayout mYearlyLayout = findViewById(R.id.parentYearly);

        RelativeLayout rl_FreeLayout = findViewById(R.id.rl_parentFree);
        RelativeLayout rl_mProLayout = findViewById(R.id.rl_parentPro);
        RelativeLayout rl_mWeeklyLayout = findViewById(R.id.rl_parentweekly);
        RelativeLayout rl_mMonthlyLayout = findViewById(R.id.rl_parentMonthly);
        RelativeLayout rl_mHalfYearLayout = findViewById(R.id.rl_parentHalfYear);
        RelativeLayout rl_mYearlyLayout = findViewById(R.id.rl_parentYearly);

        Button btn_pro = findViewById(R.id.btn_pro);
        Button btn_weekly = findViewById(R.id.btn_weekly);
        Button btn_monthly = findViewById(R.id.btn_monthly);
        Button btn_yearly = findViewById(R.id.btn_yearly);
        Button btn_halfyearly = findViewById(R.id.btn_halfyearly);

        btn_pro.setOnClickListener(mOnCliCkListener);
        btn_weekly.setOnClickListener(mOnCliCkListener);
        btn_monthly.setOnClickListener(mOnCliCkListener);
        btn_yearly.setOnClickListener(mOnCliCkListener);
        btn_halfyearly.setOnClickListener(mOnCliCkListener);

        mFreeLayout.setOnClickListener(mOnCliCkListener);
        mWeeklyLayout.setOnClickListener(mOnCliCkListener);
        mMonthlyLayout.setOnClickListener(mOnCliCkListener);
        mProLayout.setOnClickListener(mOnCliCkListener);
        mHalfYearLayout.setOnClickListener(mOnCliCkListener);
        mYearlyLayout.setOnClickListener(mOnCliCkListener);

        if (Slave.IS_PRO) {
            rl_mWeeklyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mMonthlyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mHalfYearLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mYearlyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_FreeLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));

            rl_mProLayout.setBackgroundResource(R.drawable.corner_color);


        } else if (Slave.IS_YEARLY) {
            rl_mWeeklyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mMonthlyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mHalfYearLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_FreeLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));

            rl_mYearlyLayout.setBackgroundResource(R.drawable.corner_color);
        } else if (Slave.IS_HALFYEARLY) {
            rl_mWeeklyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_mMonthlyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_FreeLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));

            rl_mHalfYearLayout.setBackgroundResource(R.drawable.corner_color);
        } else if (Slave.IS_MONTHLY) {
            rl_mWeeklyLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
            rl_FreeLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));

            rl_mMonthlyLayout.setBackgroundResource(R.drawable.corner_color);
        } else if (Slave.IS_WEEKLY) {
            rl_mWeeklyLayout.setBackgroundResource(R.drawable.corner_color);
            rl_FreeLayout.setBackgroundColor(getResources().getColor(R.color.color_disable));
        } else {
            rl_FreeLayout.setBackgroundResource(R.drawable.corner_color);
        }


        for (int i = 0; i < mBillingList.size(); i++) {
            if (mBillingList.get(i).billing_type.equalsIgnoreCase("pro")) {
                mProLayout.setVisibility(View.VISIBLE);

                ImageView ivPro = findViewById(R.id.iv_pro);
                TextView tvProTitle = findViewById(R.id.tv_pro_title);
                TextView tvProSubTitle = findViewById(R.id.tv_pro_subtitle);
                TextView tvProPrice = findViewById(R.id.tv_price_pro);
                ImageView ivOffer = findViewById(R.id.iv_offer_pro);

                Picasso.get().load(mBillingList.get(i).feature_src).into(ivPro);
                tvProTitle.setText(mBillingList.get(i).product_offer_text);
                tvProSubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvProPrice.setText(Html.fromHtml(mBillingList.get(i).product_price));

                if (mBillingList.get(i).product_offer_status) {
                    ivOffer.setVisibility(View.VISIBLE);
                    if (mBillingList.get(i).product_offer_src != null && !mBillingList.get(i).product_offer_src.equalsIgnoreCase(""))
                        Picasso.get().load(mBillingList.get(i).product_offer_src).into(ivOffer);
                }


            } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("weekly")) {

                mWeeklyLayout.setVisibility(View.VISIBLE);

                ImageView ivWeekly = findViewById(R.id.iv_weekly);
                TextView tvWeeklyTitle = findViewById(R.id.tv_weekly_title);
                TextView tvWeeklySubTitle = findViewById(R.id.tv_weekly_subtitle);
                TextView tvWeeklyPrice = findViewById(R.id.tv_weekly_price);
                ImageView ivOffer = findViewById(R.id.iv_offer_weekly);


                Picasso.get().load(mBillingList.get(i).feature_src).into(ivWeekly);
                tvWeeklyTitle.setText(mBillingList.get(i).product_offer_text);
                tvWeeklySubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvWeeklyPrice.setText(Html.fromHtml(mBillingList.get(i).product_price));

                if (mBillingList.get(i).product_offer_status) {
                    ivOffer.setVisibility(View.VISIBLE);
                    if (mBillingList.get(i).product_offer_src != null && !mBillingList.get(i).product_offer_src.equalsIgnoreCase(""))
                        Picasso.get().load(mBillingList.get(i).product_offer_src).into(ivOffer);
                }


            } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("monthly")) {

                mMonthlyLayout.setVisibility(View.VISIBLE);

                ImageView ivMonthly = findViewById(R.id.iv_monthly);
                TextView tvMonthlyTitle = findViewById(R.id.tv_monthly_title);
                TextView tvMonthlySubTitle = findViewById(R.id.tv_monthly_subTitle);
                TextView tvMonthlyPrice = findViewById(R.id.tv_monthly_price);
                ImageView ivOffer = findViewById(R.id.iv_offer_monthly);


                Picasso.get().load(mBillingList.get(i).feature_src).into(ivMonthly);
                tvMonthlyTitle.setText(mBillingList.get(i).product_offer_text);
                tvMonthlySubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvMonthlyPrice.setText(Html.fromHtml(mBillingList.get(i).product_price));

                if (mBillingList.get(i).product_offer_status) {
                    ivOffer.setVisibility(View.VISIBLE);
                    if (mBillingList.get(i).product_offer_src != null && !mBillingList.get(i).product_offer_src.equalsIgnoreCase(""))
                        Picasso.get().load(mBillingList.get(i).product_offer_src).into(ivOffer);
                }


            } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("halfYear")) {

                mHalfYearLayout.setVisibility(View.VISIBLE);

                ImageView ivHalfYear = findViewById(R.id.iv_halfYear);
                TextView tvHalfYearTitle = findViewById(R.id.tv_halfYear_title);
                TextView tvHalfYearSubTitle = findViewById(R.id.tv_halfYear_subTitle);
                TextView tvHalfYearPrice = findViewById(R.id.tv_halfYear_price);
                ImageView ivOffer = findViewById(R.id.iv_offer_halfYear);


                Picasso.get().load(mBillingList.get(i).feature_src).into(ivHalfYear);
                tvHalfYearTitle.setText(mBillingList.get(i).product_offer_text);
                tvHalfYearSubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvHalfYearPrice.setText(Html.fromHtml(mBillingList.get(i).product_price));

                if (mBillingList.get(i).product_offer_status) {
                    ivOffer.setVisibility(View.VISIBLE);
                    if (mBillingList.get(i).product_offer_src != null && !mBillingList.get(i).product_offer_src.equalsIgnoreCase(""))
                        Picasso.get().load(mBillingList.get(i).product_offer_src).into(ivOffer);
                }


            } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("yearly")) {

                mYearlyLayout.setVisibility(View.VISIBLE);

                ImageView ivYear = findViewById(R.id.iv_Year);
                TextView tvYearTitle = findViewById(R.id.tv_Year_title);
                TextView tvYearSubTitle = findViewById(R.id.tv_Year_subTitle);
                TextView tvYearPrice = findViewById(R.id.tv_Year_price);

                Picasso.get().load(mBillingList.get(i).feature_src).into(ivYear);
                tvYearTitle.setText(mBillingList.get(i).product_offer_text);
                tvYearSubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvYearPrice.setText(Html.fromHtml(mBillingList.get(i).product_price));

                ImageView ivOffer = findViewById(R.id.iv_offer_Year);

                if (mBillingList.get(i).product_offer_status) {
                    ivOffer.setVisibility(View.VISIBLE);
                    if (mBillingList.get(i).product_offer_src != null && !mBillingList.get(i).product_offer_src.equalsIgnoreCase(""))
                        Picasso.get().load(mBillingList.get(i).product_offer_src).into(ivOffer);
                }


            } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("free")) {

                mFreeLayout.setVisibility(View.VISIBLE);

                ImageView ivFree = findViewById(R.id.iv_free);
                TextView tvFreeTitle = findViewById(R.id.tv_free_title);
                TextView tvFreeSubTitle = findViewById(R.id.tv_free_subtitle);
                TextView tvFreePrice = findViewById(R.id.tv_price_free);

                Picasso.get().load(mBillingList.get(i).feature_src).into(ivFree);
                tvFreeTitle.setText(mBillingList.get(i).product_offer_text);
                tvFreeSubTitle.setText(mBillingList.get(i).product_offer_sub_text);
                tvFreePrice.setText(Html.fromHtml(mBillingList.get(i).product_price));
            }


        }

    }

    View.OnClickListener mOnCliCkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.parentFree) {
                if (Slave.hasPurchased(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("free")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }

            } else if (view.getId() == R.id.parentPro) {
                if (Slave.hasPurchasedPro(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("pro")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            } else if (view.getId() == R.id.parentweekly) {
                if (Slave.hasPurchasedMonthly(BillingListActivity.this) ||
                        Slave.hasPurchasedHalfYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedWeekly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("weekly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }

            } else if (view.getId() == R.id.parentMonthly) {
                if (Slave.hasPurchasedHalfYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedMonthly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("monthly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }

            } else if (view.getId() == R.id.parentHalfYear) {
                if (Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedHalfYearly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("halfYear")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }

            } else if (view.getId() == R.id.parentYearly) {
                if (Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("yearly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }

            } else if (view.getId() == R.id.btn_pro) {
                if (Slave.hasPurchasedPro(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("pro")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            } else if (view.getId() == R.id.btn_weekly) {
                if (Slave.hasPurchasedMonthly(BillingListActivity.this) ||
                        Slave.hasPurchasedHalfYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedWeekly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("weekly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            } else if (view.getId() == R.id.btn_monthly) {
                if (Slave.hasPurchasedHalfYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedMonthly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("monthly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            } else if (view.getId() == R.id.btn_halfyearly) {
                if (Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this) ||
                        Slave.hasPurchasedHalfYearly(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("halfYear")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            } else if (view.getId() == R.id.btn_yearly) {
                if (Slave.hasPurchasedYearly(BillingListActivity.this) ||
                        Slave.hasPurchasedPro(BillingListActivity.this)) {
                    Toast.makeText(BillingListActivity.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < mBillingList.size(); i++) {
                        if (mBillingList.get(i).billing_type.equals("yearly")) {
                            Billing b = mBillingList.get(i);

                            startActivity(new Intent(BillingListActivity.this, BillingDetailActivity.class)
                                    .putExtra("billing", b));
                        }
                    }
                }
            }
        }
    };
}
