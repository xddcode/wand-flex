package io.github.xddcode.wand.core;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/23 14:08
 */
public abstract class AbstractSource extends SimpleJavaFileObject {

    protected AbstractSource(URI uri, Kind kind) {
        super(uri, kind);
    }

    /**
     * 获取源代码的内容。
     *
     * @return
     */
    public abstract String sourceContent();

    /**
     * 获取源代码的字符内容。直接使用下载到的Java源代码字符串
     *
     * @param ignoreEncodingErrors ignore encoding errors if true
     * @return
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceContent();
    }
}
