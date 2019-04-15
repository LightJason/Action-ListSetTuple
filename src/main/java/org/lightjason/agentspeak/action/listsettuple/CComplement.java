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

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * creates the complement between collections.
 * The action uses two input arguments \f$ \mathbb{A} \f$ and \f$ \mathbb{B} \f$ and returns a
 * list of all elements which contains \f$ \mathbb{A} \setminus \mathbb{B} \f$
 *
 * {@code L = .collection/complement( [1,2,3], [3,4,5] );}
 *
 * @see https://en.wikipedia.org/wiki/Complement_(set_theory)
 */
public final class CComplement extends IBaseAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 8021131769211400348L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CComplement.class, "collection" );

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
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        if ( p_argument.get( 0 ).<Collection<?>>raw().isEmpty() && p_argument.get( 1 ).<Collection<?>>raw().isEmpty() )
            return p_context.agent().fuzzy().membership().fail();

        // all arguments must be lists, first argument is the full list
        final Collection<Object> l_result = new ArrayList<>( p_argument.get( 0 ).<Collection<Object>>raw() );
        l_result.removeAll( p_argument.get( 1 ).<Collection<Object>>raw() );
        p_return.add( CRawTerm.of( p_parallel ? Collections.synchronizedCollection( l_result ) : l_result ) );

        return Stream.empty();
    }

}
