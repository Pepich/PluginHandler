package de.pepich1851.exceptions;

public class ReferenceLoopException extends Exception
{
	public ReferenceLoopException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = 7580514418574749313L;
}
