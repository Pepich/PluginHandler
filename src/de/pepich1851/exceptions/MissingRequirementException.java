package de.pepich1851.exceptions;

public class MissingRequirementException extends Exception
{
	private static final long serialVersionUID = -5433863372712202296L;

	public MissingRequirementException(String message)
	{
		super(message);
	}
}
