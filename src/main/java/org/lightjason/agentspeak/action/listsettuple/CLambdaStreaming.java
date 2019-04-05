/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.listsettuple;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Stream;


/**
 * streaming of list
 */
public final class CLambdaStreaming implements ILambdaStreaming<Collection<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7453430804177199062L;

    @Override
    public Stream<?> apply( @Nonnull final Collection<?> p_objects )
    {
        return p_objects.stream();
    }

    @NonNull
    @Override
    public Stream<Class<?>> assignable()
    {
        return Stream.of( Collection.class );
    }
}
