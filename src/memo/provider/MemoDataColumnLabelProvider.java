package memo.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

public abstract class MemoDataColumnLabelProvider extends ColumnLabelProvider {

	public TableViewerColumn addColumnTo(TableViewer tableViewer) {
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = tableViewerColumn.getColumn();
		
		column.setMoveable(true);
		column.setResizable(true);
		column.setText(getTitle());
		column.setWidth(getWidth());
		tableViewerColumn.setLabelProvider(this);
		
		return tableViewerColumn;
	}

	@Override
	public abstract String getText(Object element);
	
	public abstract String getTitle();
	
	public abstract int getWidth();
	
}
