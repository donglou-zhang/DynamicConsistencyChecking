package com.graduate.zl.traceability.callGraph.handle;

import com.graduate.zl.traceability.common.LocConfConstant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 计算方法调用之间的“距离”
 */
public class CallDistance {

    private static final int INF = 99999;

    @Getter
    private int[][] distance;

    private PreHandleCG preHandleCG;

    private int methodNodeNum;

    private Map<String, String> conf;

    private int validCallDistance;

    private void init() {
        preHandleCG = new PreHandleCG();
        this.methodNodeNum = preHandleCG.getMethodCallNodes().size();
        this.distance = new int[this.methodNodeNum+1][this.methodNodeNum+1];
        this.conf = LocConfConstant.getLocConf();
        this.validCallDistance = Integer.parseInt(this.conf.get("validCallDistance"));
        arrayInit();
    }

    private void arrayInit() {
        for(int i=1; i<=this.methodNodeNum; i++) {
            for(int j=1; j<=this.methodNodeNum; j++) {
                this.distance[i][j] = i==j ? 0 : INF;
            }
        }
        for(String methodName : this.preHandleCG.getMethodCallMap().keySet()) {
            int num1 = getNum(methodName);
            List<String> connectedMethods = this.preHandleCG.getMethodCallMap().get(methodName);
            for(String connectedMethodName : connectedMethods) {
                int num2 = getNum(connectedMethodName);
                this.distance[num1][num2] = 1;
            }
        }
    }

    private int getNum(String methodName) {
        int ret = -1;
        for(String mn : this.preHandleCG.getMethodCallNodes().keySet()) {
            if(methodName.equals(mn)) {
                ret = this.preHandleCG.getMethodCallNodes().get(methodName);
                break;
            }
        }
        return ret;
    }

    private String getMethodName(int num) {
        String ret = null;
        for(String mn : this.preHandleCG.getMethodCallNodes().keySet()) {
            if(this.preHandleCG.getMethodCallNodes().get(mn)==num) {
                ret = mn;
                break;
            }
        }
        return ret;
    }

    public CallDistance() {
        init();
        calculateDistance();
    }

    public void calculateDistance() {
        for(int k=1; k<=this.methodNodeNum; k++) {
            for(int i=1; i<=this.methodNodeNum; i++) {
                for(int j=1; j<=this.methodNodeNum; j++) {
                    if(this.distance[i][j] > this.distance[i][k] + this.distance[k][j]) {
                        this.distance[i][j] = this.distance[i][k] + this.distance[k][j];
                    }
                }
            }
        }
    }

    /**
     * 获取有效调用距离内的相关调用方法
     * @param method com.atm.rd.model.Response:getContent(class:method)
     * @return
     */
    public List<String> getRelatedMethods(String method) {
        List<String> ret = new ArrayList<>();
        int num1 = this.preHandleCG.getMethodCallNodes().get(method);
        for(int i=1; i<=this.methodNodeNum; i++) {
            if(i != num1 && this.distance[num1][i] < this.validCallDistance) {
                ret.add(getMethodName(i));
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        CallDistance cd = new CallDistance();
        for(int i=1;i<cd.getDistance().length;i++) {
            for(int j=1;j<cd.getDistance().length;j++) {
                System.out.print(cd.getDistance()[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("-------------------------");
        List<String> re = cd.getRelatedMethods("com.atm.rd.client.Client:sendPing");
        for(String mm : re) {
            System.out.println(mm);
        }
    }
}
