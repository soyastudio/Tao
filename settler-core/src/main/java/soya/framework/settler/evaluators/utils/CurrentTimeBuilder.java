package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component(name="current_time", arguments = "format")
public class CurrentTimeBuilder implements EvaluatorBuilder<CurrentTimeBuilder.CurrentTime> {

    @Override
    public CurrentTime build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        CurrentTime evaluator = new CurrentTime();
        if(arguments.length > 0) {
            AssignmentNode param = (AssignmentNode) arguments[0];
            evaluator.format = param.getStringValue(session.getContext());
        }
        return evaluator;
    }

    static class CurrentTime implements Evaluator {
        private String format;
        private CurrentTime() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            Date date = new Date();
            if(format != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(date);
            } else{
                return date.toString();
            }
        }
    }
}
