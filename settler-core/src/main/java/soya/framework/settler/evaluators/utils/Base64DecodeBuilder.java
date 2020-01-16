package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Component(name = "decode_base64")
public class Base64DecodeBuilder implements EvaluatorBuilder<Base64DecodeBuilder.Base64Decode> {
    @Override
    public Base64Decode build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        Base64Decode decode = new Base64Decode();
        if (arguments.length > 0) {
            AssignmentNode parameter = (AssignmentNode) arguments[0];
            decode.decompress = parameter.getBoolean(session.getContext());
        }
        return decode;
    }

    static class Base64Decode implements Evaluator {
        private boolean decompress;

        private Base64Decode() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            byte[] bin = data.getBytes();
            bin = Base64.getDecoder().decode(bin);
            if(decompress || isCompressed(bin)) {
                try {
                    return decompress(bin);
                } catch (IOException e) {
                    throw new EvaluateException(e);
                }
            } else {
                return new String(bin);
            }
        }

        public static String decompress(final byte[] compressed) throws IOException {
            final StringBuilder outStr = new StringBuilder();
            if ((compressed == null) || (compressed.length == 0)) {
                return "";
            }
            if (isCompressed(compressed)) {
                final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    outStr.append(line);
                }
            } else {
                outStr.append(compressed);
            }
            return outStr.toString();
        }

        private static boolean isCompressed(final byte[] compressed) {
            return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
        }
    }
}
