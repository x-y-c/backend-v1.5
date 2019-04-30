package yangchen.exam.service.excelservice;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/4/30 12:11
 * O(∩_∩)O)
 * <p>
 * Excel listener类；
 */
public class ExcelListener extends AnalysisEventListener {


    private static Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);
    private List<Object> datas = new ArrayList<>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        LOGGER.info("当前行：" + analysisContext.getCurrentRowNum());
        System.out.println("当前行：" + analysisContext.getCurrentRowNum());
        System.out.println(o);
        datas.add(o);


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
