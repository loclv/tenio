/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.pool;

/**
 * In an application, you can have resources that are limited or time-consuming
 * to create a new one. A solution is to create a limited resource once and
 * reuse it. The object pool design will have the mechanism to create a bulk of
 * objects to pooling use. If the requirements of resources increases, the
 * current bulk's size will be also automatically increased.
 * 
 * @author kong
 * 
 */
public interface IElementPool<Element> {

	/**
	 * @return Returns an element in the pool
	 */
	Element get();

	/**
	 * When you finished using an element, repay (free) it for the reusing
	 * 
	 * @param element the finished using element
	 */
	void repay(Element element);

	/**
	 * Clean up
	 */
	void cleanup();

	/**
	 * @return Returns the total number of element or <code>-1</code> if any
	 *         exceptions caused
	 */
	int getPoolSize();

}
