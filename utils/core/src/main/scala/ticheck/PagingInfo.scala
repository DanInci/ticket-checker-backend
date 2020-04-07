package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
object PagingInfo {

  lazy val defaultPageSize: PageSize = PageSize(20)

  lazy val maxPageSize: PageSize = PageSize(50)

  lazy val defaultPagingInfo: PagingInfo = new PagingInfo(PageNumber(0), defaultPageSize)

  def apply(pageNumber: Option[PageNumber], pageSize: Option[PageSize]): PagingInfo = {
    val correctedPageNumber = pageNumber
      .map(pn => if (pn >= 1) PageNumber(pn - 1) else PageNumber(0))
      .getOrElse(PageNumber(0)) //Frontend sends request for page 1, but it will be processed as page 0
    val correctedPageSize = pageSize
      .map(ps => if (ps < 1) defaultPageSize else if (ps > 50) maxPageSize else PageSize(ps))
      .getOrElse(defaultPageSize)
    new PagingInfo(correctedPageNumber, correctedPageSize)
  }

  def apply(offset: PageNumber, limit: PageSize): PagingInfo = new PagingInfo(offset, limit)
}
final case class PagingInfo private (offset: PageNumber, limit: PageSize)
