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

package org.lightjason.agentspeak.action.listsettuple.list;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * removes an element of the list by the index.
 * Removes an element by the list index, the first argument is the
 * list object, all other element indices which should removed, the
 * action returns the removed arguments
 *
 * {@code [A|B|C] = .collection/list/remove( L, 3, [4, [5]] );}
 */
public final class CRemove extends IBaseAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = -4708243571656002435L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CRemove.class, "collection", "list" );

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
        final List<Object> l_list = p_argument.get( 0 ).raw();
        final Set<Integer> l_removed = new HashSet<>();

        CCommon.flatten( p_argument.stream().skip( 1 ) )
               .map( ITerm::<Number>raw )
               .map( Number::intValue )
               .map( i ->
               {
                   l_removed.add( i );
                   return l_list.get( i );
               } )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        final List<Object> l_result = IntStream.range( 0, l_list.size() )
                                               .boxed()
                                               .parallel()
                                               .filter( i -> !l_removed.contains( i ) )
                                               .map( l_list::get )
                                               .collect( Collectors.toList() );

        l_list.clear();
        l_list.addAll( l_result );

        return Stream.empty();
    }

}
