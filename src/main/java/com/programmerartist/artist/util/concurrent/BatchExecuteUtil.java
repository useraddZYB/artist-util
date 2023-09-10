package com.programmerartist.artist.util.concurrent;

import com.programmerartist.artist.util.date.DateUtill;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 批量执行工具
 *
 * @author 程序员Artist
 * @date 2023-07-18
 */
public class BatchExecuteUtil {

    /**
     * 批量执行
     *
     * @param taskName2Task 任务名称 2 任务接口实现
     * @param executor      指定线程池
     * @param <T>           任务接口返回值范型
     * @return              批量任务返回值包装类
     */
    public static <T> BatchExecuteResult<T> getAll(LinkedHashMap<String, Supplier<T>> taskName2Task, Executor executor) {
        return getAll(taskName2Task, executor, null);
    }


    /**
     * 批量执行，可设置超时时间
     *
     * @param taskFlag2Task 任务唯一标志性 2 任务接口实现
     * @param executor      指定线程池
     * @param timeoutMs     超时时间，单位：毫秒
     * @param <T>           任务接口返回值范型
     * @return              批量任务返回值包装类
     */
    public static <T> BatchExecuteResult<T> getAll(LinkedHashMap<String, Supplier<T>> taskFlag2Task,
                                                   Executor executor, Long timeoutMs) {

        BatchExecuteResult<T> batchResult = BatchExecuteResult.newResult();

        final long begin = System.currentTimeMillis();

        // 批量执行
        LinkedHashMap<String, CompletableFuture<T>> taskFlag2Future = new LinkedHashMap<>();
        for(Map.Entry<String, Supplier<T>> entry : taskFlag2Task.entrySet()) {
            taskFlag2Future.put(entry.getKey(), CompletableFuture.supplyAsync(entry.getValue(), executor));
        }

        // 遍历同步取结果存储
        for(Map.Entry<String, CompletableFuture<T>> entry : taskFlag2Future.entrySet()) {
            String taskFlag = entry.getKey();

            T futureResult = null;
            try {
                if(null==timeoutMs || timeoutMs<=0) {
                    futureResult = entry.getValue().get();
                }else {
                    futureResult = entry.getValue().get(timeoutMs, TimeUnit.MILLISECONDS);
                }

                if(null != futureResult) {
                    batchResult.getTaskFlag2Result().put(taskFlag, futureResult);
                }
            } catch (Throwable e) {
                batchResult.getTaskFlag2Error().put(taskFlag, e);
            }
        }

        batchResult.setTotalCost(System.currentTimeMillis() - begin);
        return batchResult;
    }







    // ==================================================== demo & test ========================================================

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor recallExecutor = ThreadPoolFactory.newAuto(10, 20, 1000, "tpe-recall-service");
        System.out.println("hello world " + DateUtill.now());

        int all = 0;
        int size = 3;
        CompletableFuture<Integer>[] allFuture = new CompletableFuture[size];
        for(int i=1; i<=size; i++) {   //  int i=size; i>=1; i--
            final int iFinal = i;
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(
                    () -> {
                        for(int j=1; j<=size; j++) {
                            try {
                                int sleep = j*1000 + iFinal*1000;
                                Thread.sleep(sleep);

                                /*int cc = 0;
                                if(iFinal == 2) {
                                    cc = iFinal / 0;
                                    System.out.println(cc);
                                }*/

                                System.out.println(Thread.currentThread().getName() + " - sleep=" + sleep + ", i=" + iFinal + ", j=" + j + " - " + DateUtill.now());
                            } catch (InterruptedException e) {
//                                e.printStackTrace();
                                System.out.println("inner InterruptedException");
                            }
                        }

                        return iFinal;
                    },
                    recallExecutor
            );

            /*Thread.sleep(10000);
            System.out.println("before add to all " + DateUtill.now());*/

            allFuture[all++] = future;
        }

        Thread.sleep(7000);



        /*System.out.println("before allOf " + DateUtill.now());
        CompletableFuture futureAll = CompletableFuture.allOf(allFuture);   // 这句有没有影响不大

        System.out.println("after allOf " + DateUtill.now());

        futureAll.get();   // 等到最后一个future执行完，此方法才会返回
        System.out.println("future get " + DateUtill.now());
*/

        System.out.println("before for get " + DateUtill.now());
        int i = 1;
        List<Integer> allResult = new ArrayList<>();
        for(CompletableFuture<Integer> future : allFuture) {
            try {
                allResult.add(future.get());
                System.out.println("future get " + (i++) + " - allResult=" + allResult + " - " + DateUtill.now() );
            } catch (InterruptedException e) {
//                e.printStackTrace();
                System.out.println("InterruptedException");
            } catch (ExecutionException e) {
//                e.printStackTrace();
                System.out.println("ExecutionException");
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Exception");
            }
        }

        System.out.println("after all future get " + DateUtill.now());

    }

}
