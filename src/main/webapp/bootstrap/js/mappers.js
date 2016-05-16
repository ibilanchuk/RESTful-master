
  function requestParam(request){
    return {
      length: request.length,
      start: request.start,
      column: request.columns[request.order[0].column].data,
      dir: request.order[0].dir,
      value: request.search.value
      }
  }
