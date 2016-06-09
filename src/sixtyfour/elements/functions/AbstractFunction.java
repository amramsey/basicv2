package sixtyfour.elements.functions;

import java.util.Locale;

import sixtyfour.Memory;
import sixtyfour.Parser;
import sixtyfour.elements.Term;


public abstract class AbstractFunction
  implements Function
{

  protected String name;
  protected Term term;


  @SuppressWarnings("unused")
  private AbstractFunction()
  {
    //
  }


  public AbstractFunction(String name)
  {
    this.name = name.toUpperCase(Locale.ENGLISH);
  }


  public Function clone(String linePart)
  {
    try
    {
      AbstractFunction clone = this.getClass().newInstance();
      clone.name = name;
      return clone;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unable to instantiate function: " + name);
    }

  }


  @Override
  public String getName()
  {
    return name;
  }


  @Override
  public Term getTerm()
  {
    return term;
  }


  @Override
  public void setTerm(Term term)
  {
    this.term = term;
  }


  @Override
  public void parse(String linePart, Memory memory)
  {
    this.setTerm(Parser.getTerm(linePart, memory));
  }


  @Override
  public boolean isFunction(String function)
  {
    return function.trim().toUpperCase(Locale.ENGLISH).startsWith(name);
  }


  @Override
  public String toString()
  {
    return this.name + "(" + term + ")";
  }
}