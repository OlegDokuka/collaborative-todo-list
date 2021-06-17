//package nl.lawik.poc.frontend.reactredux.containers
//
//import nl.lawik.poc.frontend.reactredux.actions.SetVisibilityFilter
//import nl.lawik.poc.frontend.reactredux.components.Link
//import nl.lawik.poc.frontend.reactredux.components.LinkProps
//import nl.lawik.poc.frontend.reactredux.enums.VisibilityFilter
//import nl.lawik.poc.frontend.reactredux.reducers.State
//import react.RClass
//import react.RProps
//import react.invoke
//import react.redux.rConnect
//import redux.WrapperAction
//
//interface FilterLinkProps : RProps {
//    var filter: VisibilityFilter
//}
//
//private interface LinkStateProps : RProps {
//    var active: Boolean
//}
//
//private interface LinkDispatchProps : RProps {
//    var onClick: () -> Unit
//}
//
//val filterLink: RClass<FilterLinkProps> =
//    rConnect<State, SetVisibilityFilter, WrapperAction, FilterLinkProps, LinkStateProps, LinkDispatchProps, LinkProps>(
//        { state, ownProps ->
//            active = state.visibilityFilter == ownProps.filter
//        },
//        { dispatch, ownProps ->
//            onClick = { dispatch(SetVisibilityFilter(ownProps.filter)) }
//        }
//    )(Link::class.js.unsafeCast<RClass<LinkProps>>())
