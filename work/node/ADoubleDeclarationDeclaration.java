/* This file was generated by SableCC (http://www.sablecc.org/). */

package work.node;

import work.analysis.*;

@SuppressWarnings("nls")
public final class ADoubleDeclarationDeclaration extends PDeclaration
{
    private TDouble _double_;
    private TVar _var_;
    private TSemicolon _semicolon_;

    public ADoubleDeclarationDeclaration()
    {
        // Constructor
    }

    public ADoubleDeclarationDeclaration(
        @SuppressWarnings("hiding") TDouble _double_,
        @SuppressWarnings("hiding") TVar _var_,
        @SuppressWarnings("hiding") TSemicolon _semicolon_)
    {
        // Constructor
        setDouble(_double_);

        setVar(_var_);

        setSemicolon(_semicolon_);

    }

    @Override
    public Object clone()
    {
        return new ADoubleDeclarationDeclaration(
            cloneNode(this._double_),
            cloneNode(this._var_),
            cloneNode(this._semicolon_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADoubleDeclarationDeclaration(this);
    }

    public TDouble getDouble()
    {
        return this._double_;
    }

    public void setDouble(TDouble node)
    {
        if(this._double_ != null)
        {
            this._double_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._double_ = node;
    }

    public TVar getVar()
    {
        return this._var_;
    }

    public void setVar(TVar node)
    {
        if(this._var_ != null)
        {
            this._var_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._var_ = node;
    }

    public TSemicolon getSemicolon()
    {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node)
    {
        if(this._semicolon_ != null)
        {
            this._semicolon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._semicolon_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._double_)
            + toString(this._var_)
            + toString(this._semicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._double_ == child)
        {
            this._double_ = null;
            return;
        }

        if(this._var_ == child)
        {
            this._var_ = null;
            return;
        }

        if(this._semicolon_ == child)
        {
            this._semicolon_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._double_ == oldChild)
        {
            setDouble((TDouble) newChild);
            return;
        }

        if(this._var_ == oldChild)
        {
            setVar((TVar) newChild);
            return;
        }

        if(this._semicolon_ == oldChild)
        {
            setSemicolon((TSemicolon) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
