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

package org.lightjason.agentspeak.action.listsettuple.tuple;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * sets a value within a tuple.
 * The action sets the value within the first argument
 * in each tuple argument, the action never fails
 *
 * {@code .collection/tuple/set( "value", T1, T2, T3 );}
 */
public final class CSet extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8109609646339064488L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSet.class, "collection", "tuple" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        l_arguments.stream()
                   .skip( 1 )
                   .map( ITerm::<Map.Entry<Object, Object>>raw )
                   .forEach( i -> i.setValue( l_arguments.get( 0 ).raw() ) );

        return Stream.empty();
    }
}
