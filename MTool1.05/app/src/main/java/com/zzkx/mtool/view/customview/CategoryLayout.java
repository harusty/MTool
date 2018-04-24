package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by L.K.X on 2015/11/11.
 */
public class CategoryLayout extends RelativeLayout {
    private int horizontalSpacing = 8;//水平间距
    private int verticalSpacing = 12;//行与行的垂直间距
    //用于存放所有line对象
    private ArrayList<Line> lineList = new ArrayList<Line>();
    private int mMaxLine;
    private boolean mGridMode;

    public CategoryLayout(Context context) {
        super(context);
    }

    public CategoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置水平间距
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    /**
     * 设置垂直间距
     *
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setGridMode() {
        mGridMode = true;
    }

    /**
     * 遍历子View，进行分行的操作,就是排座位
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //清空lineList
        lineList.clear();
        //1.获取总宽度,其实包含paddingLeft和paddingRight
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //2.得打除去padding的宽度,就是我们用于实际比较的总宽度
        int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();

        //3.遍历所有子view，进行分行
        Line line = null;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);//获取当前子view
            if(mGridMode){
                int size = (noPaddingWidth - horizontalSpacing * 2)/3;
                childView.getLayoutParams().width = size;
            }
            //这里的测量交给父类完成，不然子view无法正常测量自己，布局中设置宽高也无效，暂时不知道为什么

            if (line == null) {
                line = new Line();//如果不换行，还是同一个line，如果换行就变了
            }
            //4.如果当前line一个子view都木有，则直接放入line，不用比较，因为保证每行至少有一个子View
            if (line.getViewList().size() == 0) {
                line.addChild(childView);
            } else if (line.getWidth() + horizontalSpacing + childView.getMeasuredWidth() > noPaddingWidth) {
                //5.如果当前line的宽+水平间距+childView宽如果大于noPaddingWidth,需要换行
                lineList.add(line);//先将之前的line存放起来
                if (mMaxLine > 0 && lineList.size() > mMaxLine)
                    break;

                line = new Line();//重新创建line，将childView放入到新行中
                line.addChild(childView);
            } else {
                //6.说明当前childView应该属于当前行,
                line.addChild(childView);
            }

            //7.如果当前childView是最后一个子View，会造成最后一行line丢失
            if (i == (getChildCount() - 1)) {
                lineList.add(line);//将最后的一行line存放起来
                if (mMaxLine > 0 && lineList.size() > mMaxLine)
                    break;
            }
        }

        //for循环结束，lineList中就存放了所有的line，
        //计算layout所有行的时候需要的高度
        int height = getPaddingTop() + getPaddingBottom();//首先加上padding值
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).getHeight();//再加上所有行的高度
        }
        height += (lineList.size() - 1) * verticalSpacing;//最后加上所有行的垂直间距

        //向父View申请所需要的宽高
        setMeasuredDimension(width, height);

        if (getChildCount() == 0) {
            setMeasuredDimension(width, 0);
        }

    }

    /**
     * 对lineList中的line的所有子View进行布局，就是让每个人坐到自己的位置上
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);//获取到当前的line对象

            //后面每一行的top值要相应的增加,当前行的top是上一行的top值+height+垂直间距
            if (i > 0) {
                paddingTop += lineList.get(i - 1).getHeight() + verticalSpacing;
            }

            ArrayList<View> viewList = line.getViewList();//获取line的子View集合

//            //1.计算出留白的区域:noPaddingWidht-当前line的宽度
            float remainSpace = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getWidth();
//            //2.计算出每个子View平均分到的宽度
            float perSpace = remainSpace / viewList.size();

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);//获取当前的子View
//                //3.将每个子View得到宽度增加到原来的宽度上面
                int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth() + perSpace), MeasureSpec.EXACTLY);
//                childView.measure(widthMeasureSpec, 0);//重新测量childView

                if (j == 0) {
                    //每行的第一个子View,需要靠左边摆放
                    childView.layout(paddingLeft, paddingTop, paddingLeft + childView.getMeasuredWidth(),
                            paddingTop + childView.getMeasuredHeight());
                } else {
                    //摆放后面的子View，需要参考前一个子View的right
                    View preView = viewList.get(j - 1);//获取前一个子View
                    int left = preView.getRight() + horizontalSpacing;//前一个VIew的right+水平间距
                    childView.layout(left, preView.getTop(), left + childView.getMeasuredWidth(), preView.getBottom());
                }
            }
        }
    }

    public void setMaxLine(int maxLine) {
        mMaxLine = maxLine - 1;
    }



    /**
     * 封装每行的数据，包括每行的子View，行宽和行高
     *
     * @author Administrator
     */
    class Line {
        private ArrayList<View> viewList;//用来存放当前行的所有子TextView
        private int width;//当前行的所有子View的宽+水平间距
        private int height;//当前行的行高

        public Line() {
            viewList = new ArrayList<View>();
        }

        /**
         * 存放子view到viewList中
         *
         * @param view
         */
        public void addChild(View view) {
            if (!viewList.contains(view)) {

                //更新宽高
                if (viewList.size() == 0) {
                    //说明没有子view，当前view是第一个加入的，此时是不需要加horizontalSpacing
                    width = view.getMeasuredWidth();
                } else {
                    //说明不是第一个，那么需要加上水平间距
                    width += horizontalSpacing + view.getMeasuredWidth();
                }
                //height应该是所有子view中高度最大的那个
                height = Math.max(view.getMeasuredHeight(), height);

                viewList.add(view);
            }
        }

        /**
         * 获取子View的集合
         *
         * @return
         */
        public ArrayList<View> getViewList() {
            return viewList;
        }

        /**
         * 获取当前行的宽度
         *
         * @return
         */
        public int getWidth() {
            return width;
        }

        /**
         * 获取行高
         *
         * @return
         */
        public int getHeight() {
            return height;
        }
    }

}
