/*
 * This software and all files contained in it are distrubted under the MIT license.
 * 
 * Copyright (c) 2013 Cogito Learning Ltd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.crazy_putting.game.FormulaParser;

/**
 * Test the FormulaParser
 */
public class Test
{


  public static void main(String[] args)
  {
    
    String exprstr = "2*x^2 + y";
    if (args.length>0) exprstr = args[0];
    
    FormulaParser parser = new FormulaParser();
    try
    {
      ExpressionNode expr = parser.parse(exprstr);
      expr.accept(new SetVariable("x", 2));
      expr.accept(new SetVariable("y", 1));
      System.out.println("The value of the expression is "+expr.getValue());
      
    }
    catch (ParserException e)
    {
      System.out.println(e.getMessage());
    }
    catch (EvaluationException e)
    {
      System.out.println(e.getMessage());
    }
  }
}