//package com.example.strategotest.Stratego.views;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.view.SurfaceView;
//
//import com.example.strategotest.R;
//
//public class MySurfaceView extends SurfaceView {
//
//    //Number of characters of each type starting with flag ending with bomb and spy respectively
//    //This will help randomly place pieces
//    //I removed 3 bombs
//    private Integer[] playerArray = {1,1,1,2,3,2,2,2,2,8,3,1};
//
//    public MySurfaceView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setWillNotDraw(false);
//    }
//    public void onDraw(Canvas canvas){
//        //Paint
//        Paint red = new Paint();
//        red.setColor(0xffff0000);
//        Paint black = new Paint();
//        black.setColor(0xff000000);
//        black.setTextSize(100);
//
//        //Characters
////        Bitmap Bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
////        Bitmap Spy = BitmapFactory.decodeResource(getResources(),R.drawable.spy);
////        Bitmap Scout = BitmapFactory.decodeResource(getResources(),R.drawable.scout);
////        Bitmap Miner = BitmapFactory.decodeResource(getResources(),R.drawable.miner);
////        Bitmap  General = BitmapFactory.decodeResource(getResources(),R.drawable.gen);
////        Bitmap Captain = BitmapFactory.decodeResource(getResources(),R.drawable.capt);
////        Bitmap Major = BitmapFactory.decodeResource(getResources(),R.drawable.maj);
////        Bitmap Colonel = BitmapFactory.decodeResource(getResources(),R.drawable.col);
////        Bitmap Sergeant = BitmapFactory.decodeResource(getResources(),R.drawable.serg);
////        Bitmap Flag = BitmapFactory.decodeResource(getResources(),R.drawable.flag);
////        Bitmap  Lieutenant = BitmapFactory.decodeResource(getResources(),R.drawable.lt);
////        Bitmap Marshall = BitmapFactory.decodeResource(getResources(),R.drawable.marsh);
////        Bitmap BlueTile = BitmapFactory.decodeResource(getResources(),R.drawable.bluetile);
//
//        //Draw board
//        for(float i = 240; i < 1440; i += 120){
//            for(float j = 120; j < 1320; j+= 120 ){
//
//                if((j==720 || j == 600) && (i == 600 || i == 480 || i == 960 || i == 1080)){
//                    canvas.drawRect(i,j,i+118,j+118,black);
//                }
//                else{
//                    canvas.drawRect(i,j,i+118,j+118,red);
//                }
//                if(j < 600){
//  //                  canvas.drawBitmap(BlueTile, 360 + 10, 360 + 10, black);
//
//                    if((j >= 360 && i != 240) ) {
//
//                    }
//                    else {
//  //                      canvas.drawBitmap(BlueTile, i + 10, j + 10, black);
//                    }
//
//                }
//                if(j > 720) {
//                    //int nrandom = (int)(Math.random()*12);
//                    int nrandom = 1;
//                    int start = nrandom;
//                    //Loop through the array to find a non zero number
//                    while(playerArray[nrandom] <= 0) {
//                        nrandom += 1;
//                        nrandom = nrandom % 12;
//                        if(nrandom == start) {
//                            nrandom = 12;
//                            break;
//                        }
//                    }
//                   if(nrandom == 0) {
//      //                 canvas.drawBitmap(Flag,i+10,j+10,black);
//                       playerArray[nrandom] -= 1;
//                   }
//                   else if(nrandom >= 1 && nrandom <= 9) {
//                       playerArray[nrandom] -= 1;
//                       if(nrandom == 9){
//                           //Place first scout
//                           if(j ==840){
//                        //       canvas.drawBitmap(Scout, i + 10, j - 480 + 10, black);
//                           }
//                           else {
//                            //   canvas.drawBitmap(Scout, i + 10, j + 10, black);
//                           }
//                       }
//                       else if(nrandom == 8){
//                           //Put first miner in position
//                           if(j ==840){
//                              // canvas.drawBitmap(Miner, i + 10, j - 480 + 10, black);
//                           }
//                           else {
//                               canvas.drawBitmap(Miner, i + 10, j + 10, black);

//                           }
//                       }
//                       else if(nrandom == 7){
//                           //Put first sergeant in position
//                           if(j ==840){
//                               canvas.drawBitmap(Sergeant, i + 10, j - 480 + 10, black);
//                           }
//                           else {
//                               canvas.drawBitmap(Sergeant, i + 10, j + 10, black);
//                           }
//
//                       }
//                       else if(nrandom == 6){
//                           canvas.drawBitmap(Lieutenant,i+10,j+10,black);
//
//                       }
//                       else if(nrandom == 5){
//                           canvas.drawBitmap(Captain,i+10,j+10,black);
//
//                       }
//                       else if(nrandom == 4){
//                           canvas.drawBitmap(Major,i+10,j+10,black);
//
//                       }
//                       else if(nrandom == 3) {
//                           canvas.drawBitmap(Colonel,i+10,j+10,black);
//
//                       }
//                       else if(nrandom == 2){
//                           canvas.drawBitmap(General,i+10,j+10,black);
//                       }
//                       else{
//                           canvas.drawBitmap(Marshall,i+10,j-240+10,black);
//                       }
//
//                   }
//                   else if(nrandom == 10) {
//                       playerArray[nrandom] -= 1;
//                       canvas.drawBitmap(Bomb,i+10,j+10,black);
//                   }
//                   else if(nrandom == 11) {
//                       playerArray[nrandom] -= 1;
//                       canvas.drawBitmap(Spy,i+10,j+10,black);
//                   }
//
//                }
//
//
//            }
//        }
//    }
//}
//
