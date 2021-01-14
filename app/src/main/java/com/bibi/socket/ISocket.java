package com.bibi.socket;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ISocket extends Runnable {
    int MARKET = 1;
    int C2C = 2;
    int GETCHAT = 3;
    int GROUP = 3;
    int HEART = 4;

    void sendRequest(CMD cmd, byte[] body, TCPCallback callback);

    interface TCPCallback {
        void dataSuccess(CMD cmd, String response);

        void dataFail(int code, CMD cmd, String errorInfo);

    }


    enum CMD {
        COMMANDS_VERSION((short) 1),
        SOCKET_LOGIN((short) 11002),
        HEART_BEAT((short) 11004),
        SUBSCRIBE_SYMBOL_THUMB((short) 20001), UNSUBSCRIBE_SYMBOL_THUMB((short) 20002),
        PUSH_SYMBOL_THUMB((short) 20003), //1MIN
        //合约
        PUSH_SYMBOL_5_THUMB((short) 20004), //5MIN
        PUSH_SYMBOL_15_THUMB((short) 20005), //15MIN
        PUSH_SYMBOL_30_THUMB((short) 20006), //30MIN
        PUSH_SYMBOL_1_HOUR_THUMB((short) 20007), //1HOUR
        PUSH_SYMBOL_4_HOUR_THUMB((short) 20008), //4HOUR
        PUSH_SYMBOL_1_DAY_THUMB((short) 20009), //1DAY
        PUSH_SYMBOL_1_THUMB((short) 20010), //1MIN
        //币币
//        PUSH_SYMBOL_5_THUMB((short) 30051), //5MIN
//        PUSH_SYMBOL_15_THUMB((short) 30053), //15MIN
//        PUSH_SYMBOL_30_THUMB((short) 30055), //30MIN
//        PUSH_SYMBOL_1_HOUR_THUMB((short) 30057), //1HOUR
//        PUSH_SYMBOL_4_HOUR_THUMB((short) 30059), //4HOUR
//        PUSH_SYMBOL_1_DAY_THUMB((short) 30061), //1DAY
//        PUSH_SYMBOL_1_THUMB((short) 30010), //1MIN

        SUBSCRIBE_SYMBOL_KLINE((short) 20011), UNSUBSCRIBE_SYMBOL_KLINE((short) 20012),
        PUSH_SYMBOL_KLINE((short) 20013),
        SUBSCRIBE_EXCHANGE_TRADE((short) 20021), // 盘口信息
        UNSUBSCRIBE_EXCHANGE_TRADE((short) 20022), // 取消盘口信息
        PUSH_EXCHANGE_TRADE2((short) 20023), //成交
        PUSH_EXCHANGE_TRADE((short) 20024), // 盘口返回
        PUSH_EXCHANGE_K_LINE((short) 20025), //K线
        SUBSCRIBE_CHAT((short) 20031), UNSUBSCRIBE_CHAT((short) 20032),
        PUSH_CHAT((short) 20033),
        SEND_CHAT((short) 20034),
        PUSH_EXCHANGE_ORDER_COMPLETED((short) 20026),
        PUSH_EXCHANGE_ORDER_CANCELED((short) 20027),
        PUSH_EXCHANGE_ORDER_TRADE((short) 20028),
        SUBSCRIBE_GROUP_CHAT((short) 20035), UNSUBSCRIBE_GROUP_CHAT((short) 20036),
        PUSH_GROUP_CHAT((short) 20039), PUSH_EXCHANGE_DEPTH((short) 20029),

        //币币类
        SPOT_SUBSCRIBE_SYMBOL_THUMB((short) 20001), SPOT_UNSUBSCRIBE_SYMBOL_THUMB((short) 20002),

        SPOT_PUSH_SYMBOL_5_THUMB((short) 30004), //5MIN
        SPOT_PUSH_SYMBOL_15_THUMB((short) 30005), //15MIN
        SPOT_PUSH_SYMBOL_30_THUMB((short) 30006), //30MIN
        SPOT_PUSH_SYMBOL_1_HOUR_THUMB((short) 30007), //1HOUR
        SPOT_PUSH_SYMBOL_4_HOUR_THUMB((short) 30008), //4HOUR
        SPOT_PUSH_SYMBOL_1_DAY_THUMB((short) 30009), //1DAY
        SPOT_PUSH_SYMBOL_1_THUMB((short) 30010), //1MIN

        SPOT_SUBSCRIBE_EXCHANGE_TRADE((short) 30021), // 盘口信息
        SPOT_UNSUBSCRIBE_EXCHANGE_TRADE((short) 30022), // 取消盘口信息
        PUSH_EXCHANGE_SPOT_TRADE((short) 30024), // 币币盘口返回
        SPOT_PUSH_EXCHANGE_TRADE((short) 30023), //成交
        SPOT_PUSH_EXCHANGE_PLATE((short) 30024),// 盘口返回
        SPOT_PUSH_EXCHANGE_KLINE((short) 30025),
        SPOT_PUSH_EXCHANGE_ORDER_COMPLETED((short) 30026),
        SPOT_PUSH_EXCHANGE_ORDER_CANCELED((short) 30027),
        SPOT_PUSH_EXCHANGE_ORDER_TRADE((short) 30028),
        SPOT_PUSH_EXCHANGE_DEPTH((short) 30029); //深度

        private short code;

        CMD(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }

        public static CMD findObjByCode(short code) {
            for (CMD cmd : CMD.values()) {
                if (cmd.getCode() == code) return cmd;
            }
            return null;
        }

    }

}
