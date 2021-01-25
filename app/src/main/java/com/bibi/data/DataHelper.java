package com.bibi.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.bibi.entity.KLineEntity;
import com.bibi.entity.MinuteLineEntity;

/**
 * 数据辅助类 计算macd rsi等
 */

public class DataHelper {
    private static List<KLineEntity> allData = new ArrayList<>();
    private static int dataSize = 0;
    private static int RSIdataSize = 0;

    public static List<KLineEntity> getDatas() {
        return allData;
    }

    public static void setDatas(List<KLineEntity> datas) {
        allData = datas;
    }

    public static void addAllDatas(List<KLineEntity> datas) {
        allData.clear();
        allData.addAll(datas);
    }

    public static void addDatas(List<KLineEntity> datas) {
        allData.addAll(datas);
    }

    public static void updateDatas(List<KLineEntity> datas) {
        if (allData.size() == 0) {
            return;
        }
        if (datas != null && !datas.isEmpty()) {
            allData.remove(allData.size() - 1);
            allData.addAll(datas);
        }
    }

    public static List<KLineEntity> getALL(Context context, ArrayList<KLineEntity> datas) {
        if (datas != null) {
            DataHelper.calculate(datas);
            return datas;
        }
        return new ArrayList<>();
    }

    /**
     * 计算RSI
     *
     * @param datas
     */
    static void calculateRSI(List<KLineEntity> datas) {
        float rsi1 = 0;
        float rsi2 = 0;
        float rsi3 = 0;
        float trsi1 = 0;
        float trsi2 = 0;
        float trsi3 = 0;
        float rsi1ABSEma = 0;
        float rsi2ABSEma = 0;
        float rsi3ABSEma = 0;
        float rsi1MaxEma = 0;
        float rsi2MaxEma = 0;
        float rsi3MaxEma = 0;
        float trsi1ABSEma = 0;
        float trsi2ABSEma = 0;
        float trsi3ABSEma = 0;
        float trsi1MaxEma = 0;
        float trsi2MaxEma = 0;
        float trsi3MaxEma = 0;

        if (datas.size() == 1) {
            if (allData.size() > RSIdataSize) {
                for (int i = allData.size() - RSIdataSize; i < allData.size(); i++) {
                    KLineEntity point = allData.get(i);
                    final float closePrice = point.getClosePrice();
                    if (i == allData.size() - RSIdataSize) {
                        trsi1 = 0;
                        trsi2 = 0;
                        trsi3 = 0;
                        trsi1ABSEma = 0;
                        trsi2ABSEma = 0;
                        trsi3ABSEma = 0;
                        trsi1MaxEma = 0;
                        trsi2MaxEma = 0;
                        trsi3MaxEma = 0;
                    } else {
                        float Rmax = Math.max(0, closePrice - allData.get(i - 1).getClosePrice());
                        float RAbs = Math.abs(closePrice - allData.get(i - 1).getClosePrice());
                        trsi1MaxEma = (Rmax + (6f - 1) * trsi1MaxEma) / 6f;
                        trsi1ABSEma = (RAbs + (6f - 1) * trsi1ABSEma) / 6f;

                        trsi2MaxEma = (Rmax + (12f - 1) * trsi2MaxEma) / 12f;
                        trsi2ABSEma = (RAbs + (12f - 1) * trsi2ABSEma) / 12f;

                        trsi3MaxEma = (Rmax + (24f - 1) * trsi3MaxEma) / 24f;
                        trsi3ABSEma = (RAbs + (24f - 1) * trsi3ABSEma) / 24f;

                        trsi1 = (trsi1MaxEma / trsi1ABSEma) * 100;
                        trsi2 = (trsi2MaxEma / trsi2ABSEma) * 100;
                        trsi3 = (trsi3MaxEma / trsi3ABSEma) * 100;
                    }
                }
            }
        }
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                if (datas.size() == 1) {
                    rsi1 = trsi1;
                    rsi2 = trsi2;
                    rsi3 = trsi3;
                } else {
                    rsi1 = 0;
                    rsi2 = 0;
                    rsi3 = 0;
                    rsi1ABSEma = 0;
                    rsi2ABSEma = 0;
                    rsi3ABSEma = 0;
                    rsi1MaxEma = 0;
                    rsi2MaxEma = 0;
                    rsi3MaxEma = 0;
                }
            } else {
                float Rmax = Math.max(0, closePrice - datas.get(i - 1).getClosePrice());
                float RAbs = Math.abs(closePrice - datas.get(i - 1).getClosePrice());
                rsi1MaxEma = (Rmax + (6f - 1) * rsi1MaxEma) / 6f;
                rsi1ABSEma = (RAbs + (6f - 1) * rsi1ABSEma) / 6f;

                rsi2MaxEma = (Rmax + (12f - 1) * rsi2MaxEma) / 12f;
                rsi2ABSEma = (RAbs + (12f - 1) * rsi2ABSEma) / 12f;

                rsi3MaxEma = (Rmax + (24f - 1) * rsi3MaxEma) / 24f;
                rsi3ABSEma = (RAbs + (24f - 1) * rsi3ABSEma) / 24f;

                rsi1 = (rsi1MaxEma / rsi1ABSEma) * 100;
                rsi2 = (rsi2MaxEma / rsi2ABSEma) * 100;
                rsi3 = (rsi3MaxEma / rsi3ABSEma) * 100;
                if (i == datas.size() - 1) {
                    RSIdataSize = i;
                }
            }
            point.setRsi1(rsi1);
            point.setRsi2(rsi2);
            point.setRsi3(rsi3);
        }
    }

    /**
     * 计算kdj
     *
     * @param datas
     */
    static void calculateKDJ(List<KLineEntity> datas) {
        float k = 0;
        float d = 0;
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            int startIndex = i - 8;
            if (startIndex < 0) {
                startIndex = 0;
            }
            float max9 = Float.MIN_VALUE;
            float min9 = Float.MAX_VALUE;
            for (int index = startIndex; index <= i; index++) {
                max9 = Math.max(max9, datas.get(index).getHighPrice());
                min9 = Math.min(min9, datas.get(index).getLowPrice());

            }
            float rsv = 100f * (closePrice - min9) / (max9 - min9);
            if (i == 0) {
                k = rsv;
                d = rsv;
            } else {
                k = (rsv + 2f * k) / 3f;
                d = (k + 2f * d) / 3f;
            }
            point.setK(k);
            point.setD(d);
            point.setJ(3f * k - 2 * d);
        }

    }

    /**
     * 计算macd
     *
     * @param datas
     */
    static void calculateMACD(List<KLineEntity> datas) {
        float ema12 = 0;
        float ema26 = 0;
        float tEma12 = 0;
        float tEma26 = 0;
        float tClosePrice = 0;
        float dif = 0;
        float dea = 0;
        float macd = 0;
        float tdif = 0;
        float tdea = 0;
        float tmacd = 0;
        //更新
        if (datas.size() == 1) {
            if (allData.size() > dataSize) {
                for (int i = allData.size() - dataSize; i < allData.size(); i++) {
                    KLineEntity point = allData.get(i);
                    tClosePrice = point.getClosePrice();
                    if (i == allData.size() - dataSize) {
                        tEma12 = tClosePrice;
                        tEma26 = tClosePrice;
                    } else {
//                          EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
//                          EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                        tEma12 = tEma12 * 11f / 13f + tClosePrice * 2f / 13f;
                        tEma26 = tEma26 * 25f / 27f + tClosePrice * 2f / 27f;
                    }
                    tdif = tEma12 - tEma26;
                    tdea = tdea * 8f / 10f + tdif * 2f / 10f;
                    tmacd = (tdif - tdea) * 2f;
                }
            }
        }
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                if (datas.size() == 1) {
                    ema12 = tEma12;
                    ema26 = tEma26;
                    point.setDif(tdif);
                    point.setDea(tdea);
                    point.setMacd(tmacd);
                } else {
                    ema12 = closePrice;
                    ema26 = closePrice;
                }
            } else {
//                EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
//                EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                ema12 = ema12 * 11f / 13f + closePrice * 2f / 13f;
                ema26 = ema26 * 25f / 27f + closePrice * 2f / 27f;
                if (i == datas.size() - 1) {
                    dataSize = i;
                }
//              DIF = EMA（12） - EMA（26） 。
//              今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
//              用（DIF-DEA）*2即为MACD柱状图。
                dif = ema12 - ema26;
                dea = dea * 8f / 10f + dif * 2f / 10f;
                macd = (dif - dea) * 2f;
                point.setDif(dif);
                point.setDea(dea);
                point.setMacd(macd);
            }
        }

    }

    /**
     * 计算 BOLL 需要在计算ma之后进行
     *
     * @param datas
     */
    static void calculateBOLL(List<KLineEntity> datas) {
        float tmd = 0;
        float tup = 0;
        float tdown = 0;

        if (datas.size() == 1) {
            int n = 20;
            int i = allData.size() - 1;
            if (allData.size() < 20) {
                n = i + 1;
            }
            float md = 0;
            for (int j = i - n + 1; j <= i; j++) {
                float c = allData.get(j).getClosePrice();
                float m = allData.get(i).getMA30Price();
                float value = c - m;
                md += value * value;
            }
            md = md / (n - 1);
            md = (float) Math.sqrt(md);
            tmd = allData.get(i).getMA30Price();
            tup = tmd + 2f * md;
            tdown = tmd - 2f * md;
        }
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                if (datas.size() == 1) {
                    point.setMb(tmd);
                    point.setUp(tup);
                    point.setDn(tdown);
                } else {
                    point.setMb(closePrice);
                    point.setUp(Float.NaN);
                    point.setDn(Float.NaN);
                }
            } else {
                int n = 20;
                if (i < 20) {
                    n = i + 1;
                }
                float md = 0;
                for (int j = i - n + 1; j <= i; j++) {
                    float c = datas.get(j).getClosePrice();
                    float m = point.getMA30Price();
                    float value = c - m;
                    md += value * value;
                }
                md = md / (n - 1);
                md = (float) Math.sqrt(md);
                point.setMb(point.getMA30Price());
                point.setUp(point.getMb() + 2f * md);
                point.setDn(point.getMb() - 2f * md);
            }
        }

    }

    /**
     * 计算ma
     *
     * @param datas
     */
    public static void calculateMA30andBOLL(List<MinuteLineEntity> datas) {
        float ma30 = 0;
        for (int i = 0; i < datas.size(); i++) {
            MinuteLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            ma30 += closePrice;
            if (i >= 30) {
                ma30 -= datas.get(i - 30).getClosePrice();
                point.setMA30Price(ma30 / 30f);
            } else {
                point.setMA30Price(ma30 / (i + 1f));
            }
        }

        for (int i = 0; i < datas.size(); i++) {
            MinuteLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                point.setMb(closePrice);
            } else {
                int n = 20;
                if (i < 20) {
                    n = i + 1;
                }
                point.setMb(point.getMA30Price());
            }
        }
    }

    /**
     * 计算ma
     *
     * @param datas
     */
    public static void calculateMA(List<KLineEntity> datas) {
        float ma5 = 0;
        float ma10 = 0;
        float ma30 = 0;

        float tma5 = 0;
        float tma10 = 0;
        float tma30 = 0;
        boolean isUpdate = false;

        if (datas.size() == 1) {
            if (allData.size() > 30) {
                isUpdate = true;
                tma5 = 0;
                tma10 = 0;
                tma30 = 0;
                for (int d = 5; d > 0; d--) {
                    tma5 += allData.get(allData.size() - d).getClosePrice();
                }
                tma5 = tma5 / 5f;
                for (int d = 10; d > 0; d--) {
                    tma10 += allData.get(allData.size() - d).getClosePrice();
                }
                tma10 = tma10 / 10f;
                for (int d = 30; d > 0; d--) {
                    tma30 += allData.get(allData.size() - d).getClosePrice();
                }
                tma30 = tma30 / 30f;
            }
        } else {
            isUpdate = false;
        }
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getClosePrice();

            ma5 += closePrice;
            ma10 += closePrice;
            ma30 += closePrice;
            if (i >= 5) {
                ma5 -= datas.get(i - 5).getClosePrice();
                point.setMA5Price(ma5 / 5f);
            } else {
                if (isUpdate) {
                    point.setMA5Price(tma5);
                } else {
                    point.setMA5Price(ma5 / (i + 1f));
                }
            }
            if (i >= 10) {
                ma10 -= datas.get(i - 10).getClosePrice();
                point.setMA10Price(ma10 / 10f);
            } else {
                if (isUpdate) {
                    point.setMA10Price(tma10);
                } else {
                    point.setMA10Price(ma10 / (i + 1f));
                }
            }
            if (i >= 30) {
                ma30 -= datas.get(i - 30).getClosePrice();
                point.setMA30Price(ma30 / 30f);
            } else {
                if (isUpdate) {
                    point.setMA30Price(tma30);
                } else {
                    point.setMA30Price(ma30 / (i + 1f));
                }
                Log.e("MA30:", "MA30:" + tma30);
            }
        }

    }

    /**
     * 计算MA BOLL RSI KDJ MACD
     *
     * @param datas
     */
    static void calculate(List<KLineEntity> datas) {
        calculateMA(datas);
        calculateMACD(datas);
        calculateBOLL(datas);
        calculateRSI(datas);
        calculateKDJ(datas);
        calculateVolumeMA(datas);
    }

    private static void calculateVolumeMA(List<KLineEntity> entries) {
        float volumeMa5 = 0;
        float volumeMa10 = 0;
        for (int i = 0; i < entries.size(); i++) {
            KLineEntity entry = entries.get(i);

            volumeMa5 += entry.getVolume();
            volumeMa10 += entry.getVolume();

            if (i >= 5) {

                volumeMa5 -= entries.get(i - 5).getVolume();
                entry.setMA5Volume((volumeMa5 / 5f));
            } else {
                entry.setMA5Volume((volumeMa5 / (i + 1f)));
            }

            if (i >= 10) {
                volumeMa10 -= entries.get(i - 10).getVolume();
                entry.setMA10Volume((volumeMa10 / 5f));
            } else {
                entry.setMA10Volume((volumeMa10 / (i + 1f)));
            }
        }
    }
}
