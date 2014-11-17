package hogent.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jbuy519 on 19/08/2014.
 */
public class PuzzleView extends View {

    private static final String TAG = "Sudoku.view";

    private final Game game;

    private int tileWidth;
    private int tileHeight;

    private int cursorX;

    private int cursorY;

    private Paint background;
    private Paint hilite;
    private Paint light;
    private Paint dark;
    private Paint foreground;

    private Paint selected;

    private final Rect selRect = new Rect();

    public PuzzleView(Context context) {
        super(context);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public void init(){
        background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));

        dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        hilite = new Paint(Paint.ANTI_ALIAS_FLAG);
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

        light = new Paint(Paint.ANTI_ALIAS_FLAG);
        light.setColor(getResources().getColor(R.color.puzzle_light));

        foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextAlign(Paint.Align.CENTER);

        selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));

        cursorX = 0;
        cursorY = 0;

        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(heightMeasureSpec);
        getRect(cursorX*(measuredWidth/tileWidth),cursorY*(measuredHeight/tileHeight),selRect);
        Log.i(TAG,"Onmeasure rect : "+selRect);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private void getRect(int x, int y, Rect rect){
        rect.set((int) (x*tileWidth),(int)y*tileHeight,(int)(x*tileWidth+tileWidth),
                (int)(y*tileHeight+tileHeight));
    }

    private int measureHeight(int measureSpec){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize  = MeasureSpec.getSize(measureSpec);
        int result = specSize;
        if(specMode == MeasureSpec.UNSPECIFIED){
            result = 500;
        }else{
            result = specSize;
        }
        tileHeight = result /9;
        return result;
    }

    private int measureWidth(int measureSpec){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize  = MeasureSpec.getSize(measureSpec);
        int result = specSize;
        if(specMode == MeasureSpec.UNSPECIFIED){
            result = 500;
        }else{
            result = specSize;
        }
        tileWidth = result /9;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,0,getWidth(),getHeight(), background);

        //Minor gridlines
        for(int i = 0; i< 9; i++){
            canvas.drawLine(0, i * tileHeight, getWidth(), i * tileHeight,
                    light);
            canvas.drawLine(0, i * tileHeight + 1, getWidth(), i * tileHeight
                    + 1, hilite);
            canvas.drawLine(i * tileWidth, 0, i * tileWidth, getHeight(),
                    light);
            canvas.drawLine(i * tileWidth + 1, 0, i * tileWidth + 1,
                    getHeight(), hilite);
        }

        //Major Gridlines
        for(int i = 0; i<9; i+=3){
            canvas.drawLine(0, i * tileHeight, getWidth(), i * tileHeight,
                    dark);
            canvas.drawLine(0, i * tileHeight + 1, getWidth(), i * tileHeight
                    + 1, hilite);
            canvas.drawLine(i * tileWidth, 0, i * tileWidth, getHeight(), dark);
            canvas.drawLine(i * tileWidth + 1, 0, i * tileWidth + 1,
                    getHeight(), hilite);
        }


        canvas.drawRect(selRect,selected);
        Log.d(TAG,"Ondraw selrect : " +selRect);

        //Teken de nummers
        Paint.FontMetrics fm = foreground.getFontMetrics();
        foreground.setTextSize(tileHeight * 0.6f);
        foreground.setTextScaleX(tileWidth / tileHeight);
        float x = tileWidth/2;
        float y = tileHeight / 2 - (fm.ascent + fm.descent) / 2;
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                canvas.drawText(this.game.getTileString(i, j),i*tileWidth+x,j*tileHeight+y,foreground);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyDown" + keyCode);
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                select(cursorX,cursorY-1);
                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(cursorX,cursorY+1);
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(cursorX-1,cursorY);
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(cursorX+1,cursorY);
                return true;

            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: setSelectedTile(0);
                return true;

            case KeyEvent.KEYCODE_1: setSelectedTile(1);
                return true;

            case KeyEvent.KEYCODE_2: setSelectedTile(2);
                return true;

            case KeyEvent.KEYCODE_3: setSelectedTile(3);
                return true;

            case KeyEvent.KEYCODE_4: setSelectedTile(4);
                return true;

            case KeyEvent.KEYCODE_5: setSelectedTile(5);
                return true;

            case KeyEvent.KEYCODE_6: setSelectedTile(6);
                return true;

            case KeyEvent.KEYCODE_7: setSelectedTile(7);
                return true;

            case KeyEvent.KEYCODE_8: setSelectedTile(8);
                return true;

            case KeyEvent.KEYCODE_9: setSelectedTile(9);
                return true;

            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //game.showKeypadOrError(cursorX, cursorY);
                break;
            default:
                return super.onKeyDown(keyCode,event);
        }
    return false;
    }

    public void setSelectedTile(int tile) {
        if (game.setTileIfValid(cursorX, cursorY, tile)){
            invalidate();// may change hints
        } else {
        // Number is not valid for this tile
            Log.d(TAG, "setSelectedTile: invalid: " + tile);
        }
    }

    private void select(int x, int y){
        //Is heel belangrijk!
        invalidate(selRect);
        cursorX = Math.min(Math.max(x,0),8);
        cursorY = Math.min(Math.max(y,0),8);
        getRect(cursorX,cursorY,selRect);
        //Is ook heel belangrijk
        invalidate(selRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        select((int) (event.getX() / tileWidth),
                (int) (event.getY() / tileHeight));
        game.showKeypadOrError(cursorX, cursorY);
        Log.d(TAG, "onTouchEvent: x " + cursorX + ", y " + cursorY);
        return true;
    }


}
