/* This file was generated by SableCC (http://www.sablecc.org/). */

package work.node;

import work.analysis.*;

@SuppressWarnings("nls")
public final class TStringLiteral extends Token
{
    public TStringLiteral(String text)
    {
        setText(text);
    }

    public TStringLiteral(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TStringLiteral(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTStringLiteral(this);
    }
}
